import React, { useEffect, useRef, useState } from 'react';
import { Button, Paper, Typography, Box, Container, Modal, FormControl, TextField, Divider, InputLabel, Checkbox, MenuItem, Select, Grid, FormGroup, FormControlLabel, Switch } from '@mui/material';
import * as THREE from 'three';
import { OrbitControls } from 'three/examples/jsm/controls/OrbitControls';
import { DragControls } from 'three/examples/jsm/controls/DragControls';
import { calculate } from './hooks/useCalculation';
import { useContextMenu } from './hooks/useContextMenu';
import { useObjectAdder } from './hooks/useObjectAdder';

const presetColors = [
  0xff5733,
  0x33ff57,
  0x3357ff,
  0xffff33,
  0xff33ff,
  0x33ffff,
  0xffa533,
  0x9933ff,
  0x33ff99,
  0xff3399
];

const drawLine = (scene, point1, point2, color = 0xff0000) => {
  if (!scene || !point1 || !point2) return
  const material = new THREE.LineBasicMaterial({ color: color });
  const geometry = new THREE.BufferGeometry().setFromPoints([
    new THREE.Vector3(point1.x, point1.y, point1.z),
    new THREE.Vector3(point2.x, point2.y, point2.z)
  ]);
  const line = new THREE.Line(geometry, material);
  line.userData.isPathLine = true;
  scene.add(line);
};

function clearAllLines(scene) {
  const linesToRemove = [];
  if (!scene) return
  scene.traverse((object) => {
    if (object instanceof THREE.Line && object.userData.isPathLine) {
      linesToRemove.push(object);
    }
  });

  linesToRemove.forEach((line) => {
    scene.remove(line);
    if (line.geometry) {
      line.geometry.dispose();
    }
    if (line.material) {
      line.material.dispose();
    }
  });
}
function App() {
  const mountRef = useRef(null);
  const [scene, setScene] = useState(null);
  const [camera, setCamera] = useState(null);
  const [renderer, setRenderer] = useState(null);
  const [dragControls, setDragControls] = useState(null);
  const [orbitControls, setOrbitControls] = useState(null);
  const [buildingModalOpen, setBuildingModalOpen] = useState(false);
  const [buildingDimensions, setBuildingDimensions] = useState({ width: 4, height: 4, depth: 4 });
  const [order, setOrder] = useState([0])
  const [orderedPath, setOrderedPath] = useState({
    order0: [],
    order1: [],
    order2: [],
    order3: [],
    order4: [],
    order5: []
  })
  const vertices = useRef(null)
  const labelsRef = useRef([]);
  const [showVertices, setShowVertices] = useState({
    buildings: false,
    paths: false
  });


  useEffect(() => {
    // 场景设置
    const newScene = new THREE.Scene();

    // 相机设置
    const newCamera = new THREE.PerspectiveCamera(75, window.innerWidth / window.innerHeight, 0.1, 1000);
    newCamera.position.set(10, 10, 10);
    newCamera.lookAt(0, 0, 0);

    // 渲染器设置
    const newRenderer = new THREE.WebGLRenderer({ antialias: true });
    newRenderer.setSize(window.innerWidth, window.innerHeight);
    newRenderer.domElement.style.width = '100%';
    newRenderer.domElement.style.height = '100%';
    newRenderer.shadowMap.enabled = true;
    newRenderer.setClearColor(0x20232a);
    mountRef.current.appendChild(newRenderer.domElement);

    // 轨道控制器
    const newOrbitControls = new OrbitControls(newCamera, newRenderer.domElement);

    // 拖拽控制器
    const newDragControls = new DragControls([], newCamera, newRenderer.domElement);

    // 处理拖拽事件
    newDragControls.addEventListener('dragstart', () => {
      newOrbitControls.enabled = false;
    });

    newDragControls.addEventListener('dragend', () => {
      newOrbitControls.enabled = true;
    });

    // 添加更宽的网格辅助线和坐标轴
    const gridHelper = new THREE.GridHelper(50, 50);
    newScene.add(gridHelper);

    const axesHelper = new THREE.AxesHelper(10);
    newScene.add(axesHelper);

    // 添加环境光
    const ambientLight = new THREE.AmbientLight(0xffffff, 0.7);
    newScene.add(ambientLight);

    // 添加平行光
    const directionalLight = new THREE.DirectionalLight(0xffffff, 0.7);
    directionalLight.position.set(5, 10, 7.5);
    directionalLight.castShadow = true;
    newScene.add(directionalLight);

    // 渲染循环
    const animate = () => {
      requestAnimationFrame(animate);
      newOrbitControls.update();
      newRenderer.render(newScene, newCamera);
    };
    animate();

    // 更新状态
    setScene(newScene);
    setCamera(newCamera);
    setRenderer(newRenderer);
    setDragControls(newDragControls);
    setOrbitControls(newOrbitControls);

    const handleResize = () => {
      newCamera.aspect = mountRef.current.clientWidth / mountRef.current.clientHeight;
      newCamera.updateProjectionMatrix();
      newRenderer.setSize(mountRef.current.clientWidth, mountRef.current.clientHeight);
    };

    window.addEventListener('resize', handleResize);

    // 清理函数
    return () => {
      window.removeEventListener('resize', handleResize);
      if (mountRef?.current?.removeChild) {
        mountRef.current.removeChild(newRenderer.domElement);
      }
    };
  }, []);

  const { closeContextMenu } = useContextMenu(scene, camera, renderer);
  const addObject = useObjectAdder(scene, dragControls, buildingDimensions);

  const handleMenuItemClick = (type) => {
    if (type === 'building') {
      setBuildingModalOpen(true);
    } else {
      const position = new THREE.Vector3(0, 0, 0);
      addObject(type, position);
    }
    closeContextMenu();
  };

  const handleBuildingAdd = () => {
    const position = new THREE.Vector3(0, 0, 0);
    addObject('building', position);
    setBuildingModalOpen(false);
  };

  const handleDimensionChange = (dimension) => (event) => {
    setBuildingDimensions({
      ...buildingDimensions,
      [dimension]: Math.max(1, event.target.value),
    });
  };

  const classifyPaths = (paths) => {
    const classifiedPaths = {
      order0: [],
      order1: [],
      order2: [],
      order3: [],
      order4: [],
      order5: []
    };
    if (!paths?.length > 0) return
    paths.forEach((path) => {
      path.forEach(item => {
        let len = item.length
        classifiedPaths[`order${len - 1}`].push([...item])
      })
    });

    return classifiedPaths;
  };

  const getPath = () => {
    const { path, transmitters, receivers, faces } = calculate(scene, order)
    if (!path.length || transmitters.length < 1 || receivers.length < 1) return alert('at least 1 transmitter or receiver')
    vertices.current = []
    faces.forEach(item => {
      item.vertices.forEach(vertex => {
        vertices.current.push(vertex)
      })
    })
    const formatPath = classifyPaths(path)
    setOrderedPath(formatPath)
  }

  useEffect(() => {
    drawAllLines(orderedPath)
    addLabels()
  }, [orderedPath])

  useEffect(() => {
    addLabels()
  }, [showVertices])

  function drawAllLines(orderedPath) {
    clearAllLines(scene)

    drawPaths(orderedPath.order0)
    drawPaths(orderedPath.order1)
    drawPaths(orderedPath.order2)
    drawPaths(orderedPath.order3)
    drawPaths(orderedPath.order4)
    drawPaths(orderedPath.order5)


    function drawPaths(paths) {
      let color = 0
      paths.forEach(item => {
        if (color === presetColors.length) {
          color = 0
        } else {
          color++
        }
        if (item.visible === true) {
          item.forEach(i => {
            drawLine(scene, i.from, i.to, presetColors[color])
          })
        }
      })
    }
  }

  const handleCheckboxChange = (e, order, index) => {
    let checked = e.target.checked

    setOrderedPath((pre) => {
      let newPre = { ...pre }
      newPre[order][index].visible = checked
      return newPre
    })
  };
  // 摄像机位置
  const cameraPositions = [
    { name: 'Top View', position: [0, 20, 0], lookAt: [0, 0, 0] },
    { name: 'Front View', position: [0, 0, 20], lookAt: [0, 0, 0] },
    { name: 'Left View', position: [-20, 0, 0], lookAt: [0, 0, 0] },
    { name: 'Right View', position: [20, 0, 0], lookAt: [0, 0, 0] },
    { name: 'Back View', position: [0, 0, -20], lookAt: [0, 0, 0] },
  ];

  const handleCameraPositionChange = (position, lookAt) => {
    camera.position.set(...position);
    camera.lookAt(...lookAt);
    orbitControls.update();
  };

  const addLabels = () => {
    if (!vertices.current) return
    removeLabels()
    let buildingsVertices = [...vertices.current]
    let pathVertices = []

    for (let order in orderedPath) {
      if (orderedPath[order].length > 0) {
        let paths = orderedPath[order]
        paths.forEach(path => {
          if (path.visible) {
            pathVertices.push(...[path[0].from, path[0].to])
            for (let i = 1; i < path.length; i++) {
              pathVertices.push(path[i].to)
            }
          }
        })
      }
    }
    let renderVertices = []
    if (showVertices.buildings) {
      renderVertices.push(...buildingsVertices)
    }
    if (showVertices.paths) {
      renderVertices.push(...pathVertices)
    }
    console.log('pathVertices', pathVertices);
    renderVertices.forEach((vertex) => {
      const label = makeTextSprite(`(${vertex.x.toFixed(2)}, ${vertex.y.toFixed(2)}, ${vertex.z.toFixed(2)})`);
      label.position.set(vertex.x + 0.2, vertex.y + 0.2, vertex.z);
      scene.add(label);
      labelsRef.current.push(label);
    });
  };

  const removeLabels = () => {
    labelsRef.current.forEach(label => {
      scene.remove(label);
    });
    labelsRef.current = [];
  };

  const makeTextSprite = (message) => {
    const canvas = document.createElement('canvas');
    const context = canvas.getContext('2d');
    context.font = '16px Arial';

    const metrics = context.measureText(message);
    const textWidth = metrics.width;

    canvas.width = textWidth + 10;
    canvas.height = 24;

    context.font = '16px Arial';
    context.fillStyle = 'rgba(0, 0, 0, 0.7)';
    context.fillRect(0, 0, canvas.width, canvas.height);

    context.fillStyle = 'rgba(255, 255, 255, 1.0)';
    context.textBaseline = 'middle';
    context.textAlign = 'center';

    context.fillText(message, canvas.width / 2, canvas.height / 2);

    const texture = new THREE.CanvasTexture(canvas);
    const spriteMaterial = new THREE.SpriteMaterial({ map: texture });
    const sprite = new THREE.Sprite(spriteMaterial);

    const ratio = canvas.width / canvas.height;
    sprite.scale.set(ratio * 0.5, 0.5, 1);

    sprite.userData.originalScale = { x: ratio * 0.5, y: 0.5 };

    return sprite;
  };

  return (
    <Container maxWidth="xl" sx={{ display: 'flex', height: '100vh', padding: 2 }}>
      <Box sx={{ flex: 1, display: 'flex', flexDirection: 'column', height: '100%' }}>
        <div>
          <p>Calculation Result</p>
          <Box>
            {Object.keys(orderedPath).map((order, i) => (
              orderedPath[order].length > 0 && (
                <Grid container key={i} spacing={2} alignItems="center">
                  <Grid item xs={2}>
                    <Typography variant="body1">{order}:</Typography>
                  </Grid>
                  <Grid item xs={10}>
                    <Grid container spacing={1} alignItems="center">
                      {orderedPath[order].map((item, index) => (
                        <Grid item key={order + index + Date.now()} xs={3} style={{
                          display: 'flex',
                          alignItems: 'center'
                        }}>
                          <Typography variant="body1">Path{index + 1}</Typography>
                          <Checkbox checked={item.visible} onChange={(e) => handleCheckboxChange(e, order, index)} />
                        </Grid>
                      ))}
                    </Grid>
                  </Grid>
                </Grid>
              )
            ))}
          </Box>
          <Divider></Divider>
        </div>
        <div ref={mountRef} style={{ flex: 1 }} />
      </Box>
      <Paper elevation={3} sx={{ padding: '10px', width: '200px', display: 'flex', flexDirection: 'column' }}>
        <Typography variant="h6">Control Panel</Typography>

        <Button
          variant="contained"
          onClick={getPath}
          sx={{ marginBottom: '10px' }}
          color="primary"
        >
          Calculate
        </Button>
        <FormGroup>
          <FormControlLabel
            control={
              <Switch
                checked={showVertices.buildings}
                onChange={(event) => setShowVertices(prev => ({
                  ...prev,
                  buildings: event.target.checked
                }))}
              />
            }
            label="Show building vertices"
          />
          <FormControlLabel
            control={
              <Switch
                checked={showVertices.paths}
                onChange={(event) => setShowVertices(prev => ({
                  ...prev,
                  paths: event.target.checked
                }))}
              />
            }
            label="Show path vertices"
          />
        </FormGroup>
        <Divider sx={{ marginBottom: '10px' }} />

        <FormControl fullWidth sx={{ marginBottom: '10px' }}>
          <InputLabel id="order-select-label">Order</InputLabel>
          <Select
            labelId="order-select-label"
            multiple
            value={order}
            label="Order"
            onChange={(e) => setOrder(e.target.value)}
          >
            {[0, 1, 2, 3, 4, 5].map((value) => (
              <MenuItem key={value} value={value}>{value}</MenuItem>
            ))}
          </Select>
        </FormControl>

        <Typography variant="h6">Add Objects</Typography>

        <Button
          variant="contained"
          onClick={() => handleMenuItemClick('transmitter')}
          sx={{ marginTop: '10px' }}
          color="secondary"
        >
          Add Transmitter
        </Button>
        <Button
          variant="contained"
          onClick={() => handleMenuItemClick('receiver')}
          sx={{ marginTop: '10px' }}
          color="secondary"
        >
          Add Receiver
        </Button>
        <Button
          variant="contained"
          onClick={() => handleMenuItemClick('building')}
          sx={{ marginTop: '10px' }}
          color="secondary"
        >
          Add Building
        </Button>

        <Divider sx={{ margin: '20px 0' }} />

        <Typography variant="h6">Camera Positions</Typography>

        {cameraPositions.map((pos, index) => (
          <Button
            key={index}
            variant="contained"
            onClick={() => handleCameraPositionChange(pos.position, pos.lookAt)}
            sx={{ marginTop: '10px' }}
            color="success"
          >
            {pos.name}
          </Button>
        ))}

        <Box sx={{ flex: 1 }} />
      </Paper>
      <Modal
        open={buildingModalOpen}
        onClose={() => setBuildingModalOpen(false)}
        aria-labelledby="building-modal-title"
        aria-describedby="building-modal-description"
      >
        <Paper sx={{ padding: 2, position: 'absolute', top: '50%', left: '50%', transform: 'translate(-50%, -50%)', width: 300 }}>
          <Typography id="building-modal-title" variant="h6" component="h2">
            Set Building Dimensions
          </Typography>
          <TextField
            label="Width"
            type="number"
            fullWidth
            value={buildingDimensions.width}
            onChange={handleDimensionChange('width')}
            sx={{ marginTop: 2 }}
            min={1}
          />
          <TextField
            label="Height"
            type="number"
            fullWidth
            value={buildingDimensions.height}
            onChange={handleDimensionChange('height')}
            sx={{ marginTop: 2 }}
            min={1}
          />
          <TextField
            label="Depth"
            type="number"
            fullWidth
            value={buildingDimensions.depth}
            onChange={handleDimensionChange('depth')}
            sx={{ marginTop: 2 }}
            min={1}
          />
          <Button variant="contained" onClick={handleBuildingAdd} sx={{ marginTop: 2 }}>
            Confirm
          </Button>
        </Paper>
      </Modal>
    </Container>
  );
}

export default App;
