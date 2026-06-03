const fs = require('fs');
const path = require('path');

const SRC = 'D:/22523/ZhiXing-main';
const DST = 'D:/22523/anxin-main';

// 需要复制的源文件列表（所有 .java 文件 + pom.xml）
function collectFiles(dir, ext) {
  const results = [];
  const entries = fs.readdirSync(dir, { withFileTypes: true });
  for (const e of entries) {
    const full = path.join(dir, e.name);
    if (e.isDirectory() && e.name !== 'target' && e.name !== 'out') {
      results.push(...collectFiles(full, ext));
    } else if (e.isFile() && full.endsWith(ext)) {
      results.push(full);
    }
  }
  return results;
}

// 收集源文件和测试文件
const mainFiles = collectFiles(path.join(SRC, 'src', 'main', 'java', 'com', 'zhixing'), '.java');
const testFiles = collectFiles(path.join(SRC, 'src', 'test', 'java', 'com', 'zhixing'), '.java');

console.log(`Main source files: ${mainFiles.length}`);
console.log(`Test files: ${testFiles.length}`);

let copied = 0;
let errors = 0;

function copyAndRenameFile(srcPath) {
  // 计算目标路径: com/zhixing → com/anxin
  let rel = path.relative(path.join(SRC, 'src', 'main', 'java', 'com', 'zhixing'), srcPath);
  let isTest = false;
  if (rel === srcPath) {
    // 尝试测试路径
    rel = path.relative(path.join(SRC, 'src', 'test', 'java', 'com', 'zhixing'), srcPath);
    isTest = true;
  }
  if (rel === srcPath) return; // 不在预期的源路径中

  const testDir = isTest ? 'test' : 'main';
  const dstPath = path.join(DST, 'src', testDir, 'java', 'com', 'anxin', 'navigation', rel);

  // 确保目标目录存在
  fs.mkdirSync(path.dirname(dstPath), { recursive: true });

  // 读取源文件内容
  let content = fs.readFileSync(srcPath, 'utf-8');

  // 替换所有 com.zhixing 引用为 com.anxin
  // 这处理 package 声明、import 语句以及代码中的任何引用
  content = content.replace(/com\.zhixing/g, 'com.anxin');

  // 注意：不要替换独立出现的 "zhixing" 字符串（如变量名、注释中等）
  // 只替换 com.zhixing 模式

  fs.writeFileSync(dstPath, content);
}

// 复制所有主源文件
for (const f of mainFiles) {
  try {
    copyAndRenameFile(f);
    copied++;
  } catch (e) {
    console.error(`Error copying ${f}: ${e.message}`);
    errors++;
  }
}

// 复制所有测试文件
for (const f of testFiles) {
  try {
    copyAndRenameFile(f);
    copied++;
  } catch (e) {
    console.error(`Error copying ${f}: ${e.message}`);
    errors++;
  }
}

console.log(`\nCopied: ${copied} files`);
console.log(`Errors: ${errors}`);
