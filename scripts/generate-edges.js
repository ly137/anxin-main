const fs = require('fs');
const path = require('path');

const dataDir = 'D:/22523/ZhiXing-main/data';

// 读取顶点
const vertices = JSON.parse(fs.readFileSync(path.join(dataDir, 'vertex.json'), 'utf-8'));

// ========== 1. 修正地点类型 ==========
const typeHints = {
  '育英': 'DORMITORY',
  '新言': 'DORMITORY',
  '汶阳': 'DORMITORY',
  '东岳': 'DORMITORY',
  '海右': 'DORMITORY',
  '瞻岩宿舍': 'DORMITORY',
  '书院': 'OFFICE',
  '餐厅': 'CANTEEN',
  '食堂': 'CANTEEN',
  '教学楼': 'TEACHING_BUILDING',
  '图书馆': 'LIBRARY',
  '门': 'GATE',
  '体育公园': 'SPORTS_CENTER',
  '剧场': 'OTHER',
  '影城': 'OTHER',
  '岳动馆': 'SPORTS_CENTER',
  '岳泳馆': 'SPORTS_CENTER',
  '学生活动中心': 'OFFICE',
  '工程实训中心': 'TEACHING_BUILDING',
  '国际教育': 'TEACHING_BUILDING',
  '无用楼': 'TEACHING_BUILDING',
  '剧场': 'OTHER',
  '书院': 'OFFICE',
};

vertices.forEach(v => {
  for (const [keyword, type] of Object.entries(typeHints)) {
    if (v.name.includes(keyword) && v.type === 'OTHER') {
      v.type = type;
      break;
    }
  }
});

// 保存修正后的 vertex.json
fs.writeFileSync(path.join(dataDir, 'vertex.json'), JSON.stringify(vertices.map(v => ({
  id: v.id, name: v.name, type: v.type, x: v.x, y: v.y, description: v.description
})), null, 2));

// ========== 2. 计算边 ==========
function dist(a, b) {
  return Math.sqrt((a.x - b.x) ** 2 + (a.y - b.y) ** 2);
}

// 计算所有点对之间的距离
const pairs = [];
for (let i = 0; i < vertices.length; i++) {
  for (let j = i + 1; j < vertices.length; j++) {
    pairs.push({ i, j, dist: dist(vertices[i], vertices[j]) });
  }
}
pairs.sort((a, b) => a.dist - b.dist);

// 使用并查集确保连通性
class UF {
  constructor(n) { this.parent = Array.from({length: n}, (_, i) => i); this.rank = new Array(n).fill(0); }
  find(x) { return this.parent[x] === x ? x : (this.parent[x] = this.find(this.parent[x])); }
  union(a, b) {
    a = this.find(a); b = this.find(b);
    if (a === b) return false;
    if (this.rank[a] < this.rank[b]) [a, b] = [b, a];
    this.parent[b] = a;
    if (this.rank[a] === this.rank[b]) this.rank[a]++;
    return true;
  }
  isConnected() { const r = this.find(0); for (let i = 1; i < this.parent.length; i++) { if (this.find(i) !== r) return false; } return true; }
  components() {
    const comps = new Map();
    for (let i = 0; i < this.parent.length; i++) {
      const r = this.find(i);
      if (!comps.has(r)) comps.set(r, []);
      comps.get(r).push(i);
    }
    return Array.from(comps.values());
  }
}

const uf = new UF(vertices.length);
const edges = [];
const addedEdges = new Set();

function edgeKey(a, b) { return a < b ? `${a}-${b}` : `${b}-${a}`; }

// 第一步：Kruskal 生成最小生成树，确保全图连通
for (const {i, j, dist} of pairs) {
  if (uf.union(i, j)) {
    edges.push({
      from: i, to: j, dist,
      roadType: 'MAIN_ROAD' // 骨架用主干道
    });
    addedEdges.add(edgeKey(i, j));
    if (uf.isConnected()) break;
  }
}

console.log(`最小生成树: ${edges.length} 条边`);

// 第二步：为每个顶点补充到最近邻居的 PATH 连接
// 按距离排序，对每个顶点找 2~3 个最近的未连接邻居
vertices.forEach((v, i) => {
  const neighbors = [];
  for (let j = 0; j < vertices.length; j++) {
    if (i === j) continue;
    neighbors.push({ j, dist: dist(v, vertices[j]) });
  }
  neighbors.sort((a, b) => a.dist - b.dist);

  let added = 0;
  for (const {j, dist: d} of neighbors) {
    if (added >= 2) break;
    const key = edgeKey(i, j);
    if (!addedEdges.has(key) && d < 300) { // 只连接 300m 以内的
      edges.push({
        from: i, to: j, dist: d,
        roadType: 'PATH'
      });
      addedEdges.add(key);
      added++;
    }
  }
});

console.log(`补充小路后: ${edges.length} 条边`);

// ========== 3. 生成输出格式 ==========
const outEdges = edges.map(({from, to, dist, roadType}) => ({
  fromId: vertices[from].id,
  toId: vertices[to].id,
  weight: Math.round(dist * 100) / 100,
  oneWay: false,
  forbidden: false,
  roadType: roadType
}));

fs.writeFileSync(path.join(dataDir, 'edge.json'), JSON.stringify(outEdges, null, 2));

// ========== 4. 统计 ==========
console.log(`\n========== 生成报告 ==========`);
console.log(`地点数: ${vertices.length}`);
console.log(`道路数: ${outEdges.length}`);
console.log(`主干道: ${outEdges.filter(e => e.roadType === 'MAIN_ROAD').length}`);
console.log(`小路:   ${outEdges.filter(e => e.roadType === 'PATH').length}`);
console.log(`平均距离: ${(outEdges.reduce((s, e) => s + e.weight, 0) / outEdges.length).toFixed(1)}m`);
console.log(`最大距离: ${outEdges.reduce((max, e) => Math.max(max, e.weight), 0).toFixed(1)}m`);
console.log(`最小距离: ${outEdges.reduce((min, e) => Math.min(min, e.weight), Infinity).toFixed(1)}m`);

// 连通性验证
const uf2 = new UF(vertices.length);
outEdges.forEach(e => {
  const fi = vertices.findIndex(v => v.id === e.fromId);
  const ti = vertices.findIndex(v => v.id === e.toId);
  if (fi >= 0 && ti >= 0) uf2.union(fi, ti);
});
console.log(`全图连通: ${uf2.isConnected() ? '✅ 是' : '❌ 否（存在孤立区域）'}`);
if (!uf2.isConnected()) {
  const comps = uf2.components();
  console.log(`孤立组件: ${comps.length} 个`);
  comps.forEach((c, idx) => {
    console.log(`  组件${idx + 1}: ${c.map(i => vertices[i].name).join(', ')}`);
  });
}

// 度数统计
const degree = new Array(vertices.length).fill(0);
outEdges.forEach(e => {
  const fi = vertices.findIndex(v => v.id === e.fromId);
  const ti = vertices.findIndex(v => v.id === e.toId);
  if (fi >= 0) degree[fi]++;
  if (ti >= 0) degree[ti]++;
});
const minDeg = Math.min(...degree);
const maxDeg = Math.max(...degree);
const avgDeg = degree.reduce((s, d) => s + d, 0) / degree.length;
console.log(`度数范围: ${minDeg} ~ ${maxDeg}（平均 ${avgDeg.toFixed(1)}）`);
const lowDeg = vertices.filter((v, i) => degree[i] <= 1).map((v, i) => v.name);
if (lowDeg.length > 0) console.log(`度数≤1的地点: ${lowDeg.join(', ')}`);
