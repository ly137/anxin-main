# -*- coding: utf-8 -*-
"""生成《算法课程设计》泰山科技学院课程考核论文"""

from docx import Document
from docx.shared import Pt, Cm, Inches, RGBColor, Emu
from docx.enum.text import WD_ALIGN_PARAGRAPH
from docx.enum.table import WD_TABLE_ALIGNMENT
from docx.enum.section import WD_ORIENT
from docx.oxml.ns import qn, nsdecls
from docx.oxml import parse_xml
import copy


def set_line_spacing(paragraph, pt_val):
    """设置固定行距（磅值）"""
    pPr = paragraph._element.get_or_add_pPr()
    spacing = pPr.find(qn('w:spacing'))
    if spacing is None:
        spacing = parse_xml(f'<w:spacing {nsdecls("w")} w:line="{int(pt_val * 20)}" w:lineRule="exact"/>')
        pPr.append(spacing)
    else:
        spacing.set(qn('w:line'), str(int(pt_val * 20)))
        spacing.set(qn('w:lineRule'), 'exact')


def set_first_line_indent(paragraph, pt_val):
    """设置首行缩进（磅值）"""
    pPr = paragraph._element.get_or_add_pPr()
    ind = pPr.find(qn('w:ind'))
    if ind is None:
        ind = parse_xml(f'<w:ind {nsdecls("w")} w:firstLine="{int(pt_val * 20)}"/>')
        pPr.append(ind)
    else:
        ind.set(qn('w:firstLine'), str(int(pt_val * 20)))


def body_para(doc, text, bold=False, indent=True, alignment=WD_ALIGN_PARAGRAPH.JUSTIFY):
    """正文段落：宋体四号，不加粗，首行缩进2字符，两端对齐，行距固定值28磅"""
    p = doc.add_paragraph()
    p.alignment = alignment
    set_line_spacing(p, 28)
    if indent:
        set_first_line_indent(p, 28)  # 四号字2字符≈28pt
    run = p.add_run(text)
    run.font.name = '宋体'
    run._element.rPr.rFonts.set(qn('w:eastAsia'), '宋体')
    run.font.size = Pt(14)
    run.bold = bold
    return p


def heading1_para(doc, text):
    """一级标题：三号黑体不加粗，左缩进2字符，左对齐，行距28磅"""
    p = doc.add_paragraph()
    p.alignment = WD_ALIGN_PARAGRAPH.LEFT
    set_line_spacing(p, 28)
    set_first_line_indent(p, 32)  # 三号字2字符≈32pt
    # 段前段后间距
    pPr = p._element.get_or_add_pPr()
    before = parse_xml(f'<w:spacing {nsdecls("w")} w:before="200" w:after="100" w:line="{28*20}" w:lineRule="exact"/>')
    existing = pPr.find(qn('w:spacing'))
    if existing is not None:
        pPr.remove(existing)
    pPr.append(before)
    set_first_line_indent(p, 32)
    run = p.add_run(text)
    run.font.name = '黑体'
    run._element.rPr.rFonts.set(qn('w:eastAsia'), '黑体')
    run.font.size = Pt(16)
    run.bold = False
    return p


def heading2_para(doc, text):
    """二级标题：小三宋体加粗，左缩进2字符，左对齐，行距28磅"""
    p = doc.add_paragraph()
    p.alignment = WD_ALIGN_PARAGRAPH.LEFT
    set_line_spacing(p, 28)
    set_first_line_indent(p, 30)  # 小三号字2字符≈30pt
    pPr = p._element.get_or_add_pPr()
    before = parse_xml(f'<w:spacing {nsdecls("w")} w:before="120" w:after="60" w:line="{28*20}" w:lineRule="exact"/>')
    existing = pPr.find(qn('w:spacing'))
    if existing is not None:
        pPr.remove(existing)
    pPr.append(before)
    set_first_line_indent(p, 30)
    run = p.add_run(text)
    run.font.name = '宋体'
    run._element.rPr.rFonts.set(qn('w:eastAsia'), '宋体')
    run.font.size = Pt(15)
    run.bold = True
    return p


def heading3_para(doc, text):
    """三级标题：四号宋体不加粗，左缩进2字符，左对齐，行距28磅"""
    p = doc.add_paragraph()
    p.alignment = WD_ALIGN_PARAGRAPH.LEFT
    set_line_spacing(p, 28)
    set_first_line_indent(p, 28)
    run = p.add_run(text)
    run.font.name = '宋体'
    run._element.rPr.rFonts.set(qn('w:eastAsia'), '宋体')
    run.font.size = Pt(14)
    run.bold = False
    return p


def empty_para(doc):
    """空行"""
    p = doc.add_paragraph()
    set_line_spacing(p, 28)
    return p


def center_para(doc, text, font_name='宋体', font_size=16, bold=False):
    """居中段落"""
    p = doc.add_paragraph()
    p.alignment = WD_ALIGN_PARAGRAPH.CENTER
    set_line_spacing(p, 36)
    run = p.add_run(text)
    run.font.name = font_name
    run._element.rPr.rFonts.set(qn('w:eastAsia'), font_name)
    run.font.size = Pt(font_size)
    run.bold = bold
    return p


def add_page_break(doc):
    """添加分页符"""
    p = doc.add_paragraph()
    run = p.add_run()
    run._element.append(parse_xml(f'<w:br {nsdecls("w")} w:type="page"/>'))


# ==================== 构建文档 ====================

doc = Document()

# 设置默认字体
style = doc.styles['Normal']
style.font.name = '宋体'
style._element.rPr.rFonts.set(qn('w:eastAsia'), '宋体')
style.font.size = Pt(14)

# 设置页面为A4
for section in doc.sections:
    section.page_width = Cm(21)
    section.page_height = Cm(29.7)
    section.top_margin = Cm(2.54)
    section.bottom_margin = Cm(2.54)
    section.left_margin = Cm(3.17)
    section.right_margin = Cm(3.17)

# ==================== 封面 ====================
empty_para(doc)
empty_para(doc)
empty_para(doc)

center_para(doc, '课程考核论文', '黑体', 26, bold=True)
center_para(doc, '2025-2026学年第二学期', '宋体', 16)
empty_para(doc)
center_para(doc, '算法课程设计课程报告', '黑体', 22, bold=True)
empty_para(doc)
empty_para(doc)

info_lines = [
    '课程名称：算法课程设计',
    '学生学院：大数据学院',
    '专    业：计算机科学与技术',
    '班    级：专升本25-9',
    '学生姓名：范景琛',
    '学生学号：2025250387',
]
for line in info_lines:
    p = doc.add_paragraph()
    p.alignment = WD_ALIGN_PARAGRAPH.CENTER
    set_line_spacing(p, 40)
    run = p.add_run(line)
    run.font.name = '宋体'
    run._element.rPr.rFonts.set(qn('w:eastAsia'), '宋体')
    run.font.size = Pt(16)

empty_para(doc)
empty_para(doc)
empty_para(doc)
center_para(doc, '二〇二六年六月', '宋体', 16)

# ==================== 评分表 ====================
add_page_break(doc)

table = doc.add_table(rows=9, cols=4)
table.alignment = WD_TABLE_ALIGNMENT.CENTER

# 设置表格宽度
for row in table.rows:
    for cell in row.cells:
        tc = cell._tc
        tcPr = tc.get_or_add_tcPr()
        tblW = parse_xml(f'<w:tcW {nsdecls("w")} w:w="2340" w:type="dxa"/>')
        tcPr.append(tblW)

# 合并第一行
table.cell(0, 0).merge(table.cell(0, 3))

# 填充评分表
score_data = [
    ['考核论文评分标准及得分', '', '', ''],
    ['评分项目', '评分标准', '分值', '得分'],
    ['论文选题', '1.选题明确，紧密贴合算法课程核心知识与培养目标，立意新颖，具有较好的问题挑战性或应用背景（10-15分）\n2.选题较明确，比较贴合算法课程知识与培养目标（5-10分）\n3.选题不够明确，与算法课程知识关联度较弱（0-5分）', '15分', ''],
    ['论文内容', '1.内容充实具体，论据充分，论证严谨，能够清晰描述问题、算法设计与实现过程，详略得当（25-35分）\n2.内容较为完整，有基本的问题描述和算法说明，但深度或严谨性有所不足（15-25分）\n3.内容单薄，问题描述不清，缺乏算法实现与分析（0-15分）', '35分', ''],
    ['论文结构', '1.结构完整，层次分明，条理清晰，严格体现"提出问题、分析问题、解决问题"的完整体系（12-15分）\n2.结构基本完整，主要部分具备，但逻辑衔接或层次安排有所欠缺（8-12分）\n3.结构不完整，层次混乱，缺少关键组成部分（0-8分）', '15分', ''],
    ['论文观点', '1.观点清晰，结论明确，能够提出自己的见解，有较强的逻辑支撑，且具有一定的实践价值（12-20分）\n2.观点基本明确，多为对已有知识的复述，缺少深入分析或个人见解（8-12分）\n3.观点模糊，缺乏明确结论（0-8分）', '20分', ''],
    ['格式规范', '1.严格按照"摘要、关键词、正文"的格式顺序写作，满足论文写作要求的字数、字体、行距、标题层级等格式规范（12-15分）\n2.部分符合格式要求，存在明显不规范之处（8-12分）\n3.严重不符合格式规范要求（0-8分）', '15分', ''],
    ['总分', '', '', ''],
    ['任课教师评语：\n\n\n\n         任课教师签字：                            ', '', '', ''],
]

for ri, row_data in enumerate(score_data):
    for ci, cell_text in enumerate(row_data):
        cell = table.cell(ri, ci) if ci < len(table.rows[ri].cells) else None
        if cell is None:
            continue
        # 清除默认段落
        cell.paragraphs[0].clear()
        p = cell.paragraphs[0]
        p.alignment = WD_ALIGN_PARAGRAPH.CENTER if ci != 1 else WD_ALIGN_PARAGRAPH.LEFT
        
        # 设置边框
        tc = cell._tc
        tcPr = tc.get_or_add_tcPr()
        borders = parse_xml(
            f'<w:tcBorders {nsdecls("w")}>'
            f'<w:top w:val="single" w:sz="4" w:space="0" w:color="000000"/>'
            f'<w:left w:val="single" w:sz="4" w:space="0" w:color="000000"/>'
            f'<w:bottom w:val="single" w:sz="4" w:space="0" w:color="000000"/>'
            f'<w:right w:val="single" w:sz="4" w:space="0" w:color="000000"/>'
            f'</w:tcBorders>'
        )
        tcPr.append(borders)
        
        run = p.add_run(cell_text)
        run.font.name = '宋体'
        run._element.rPr.rFonts.set(qn('w:eastAsia'), '宋体')
        run.font.size = Pt(12)
        if ri == 0:
            run.font.name = '黑体'
            run._element.rPr.rFonts.set(qn('w:eastAsia'), '黑体')
            run.font.size = Pt(14)
            run.bold = True
        if ri == 1:
            run.bold = True

# ==================== 格式说明页 ====================
add_page_break(doc)

body_para(doc, '正文内容格式要求：')
body_para(doc, '1.正文要求：', bold=True)
body_para(doc, '使用宋体，四号，不加粗，段落首行左缩进2字符，两端对齐，回行顶格，行距固定值28磅。')
body_para(doc, '2.文中结构层次顺序：', bold=True)
body_para(doc, '一级标题使用\u201c一、\u201d\u201c二、\u201d标注，三号，黑体，不加粗，左缩进2字符，左对齐，行距固定值28磅。')
body_para(doc, '二级标题使用\u201c（一）\u201d\u201c（二）\u201d标注，小三，宋体，加粗，左缩进2字符，左对齐，行距固定值28磅。')
body_para(doc, '三级标题使用\u201c1.\u201d\u201c2.\u201d标注，四号，宋体，不加粗，左缩进2字符，左对齐，行距固定值28磅。')
body_para(doc, '四级标题使用\u201c（1）\u201d\u201c（2）\u201d标注，四号，宋体，不加粗，左缩进2字符，左对齐，行距固定值28磅。')
body_para(doc, '五级标题使用\u201c①\u201d\u201c②\u201d标注，四号，宋体，不加粗，左缩进2字符，左对齐，行距固定值28磅。')
body_para(doc, '3.从论文内容第一页（摘要）开始添加页码，格式为宋体、四号，页脚居中，页码格式为数字左右各放一条一字线（即- 1 -）；不要页眉。')

empty_para(doc)

body_para(doc, '参考文献填写要求：', bold=True)
body_para(doc, '1.格式要求')
body_para(doc, '专著格式：序号. 编著者. 书名[M]. 出版地：出版社，年代.')
body_para(doc, '期刊论文格式：序号. 作者. 论文名称[J]. 期刊名称，年度，卷（期）：起止页码.')
body_para(doc, '学位论文格式：序号. 作者. 学位论文名称[D]. 发表地：学位授予单位，年度.')
body_para(doc, '2.序号后作者姓名前空一个字符，书名或期刊名称前空一个字符，出版地前空一个字符。')

empty_para(doc)

body_para(doc, '注意：', bold=True)
body_para(doc, '1.开课单位可根据实际情况，确定论文结构的各组成部分和字数要求。')
body_para(doc, '2.论文封面与考核论文评分标准及得分页用A4白纸双面打印到一张纸上，论文内容A4白纸双面打印，左侧装订两枚订书针。')

# ==================== 正文开始（新节，带页码） ====================
# 添加分节符（新页）
new_section = doc.add_section()
new_section.page_width = Cm(21)
new_section.page_height = Cm(29.7)
new_section.top_margin = Cm(2.54)
new_section.bottom_margin = Cm(2.54)
new_section.left_margin = Cm(3.17)
new_section.right_margin = Cm(3.17)

# 去掉首页页眉
new_section.different_first_page_header_footer = False

# 添加页码（页脚居中，格式：- 1 -）
footer = new_section.footer
footer.is_linked_to_previous = False
fp = footer.paragraphs[0]
fp.alignment = WD_ALIGN_PARAGRAPH.CENTER
# 添加页码字段
run1 = fp.add_run('- ')
run1.font.name = '宋体'
run1._element.rPr.rFonts.set(qn('w:eastAsia'), '宋体')
run1.font.size = Pt(14)

# PAGE 字段
fldChar1 = parse_xml(f'<w:fldChar {nsdecls("w")} w:fldCharType="begin"/>')
instrText = parse_xml(f'<w:instrText {nsdecls("w")} xml:space="preserve"> PAGE </w:instrText>')
fldChar2 = parse_xml(f'<w:fldChar {nsdecls("w")} w:fldCharType="end"/>')

run_page = fp.add_run()
run_page.font.name = '宋体'
run_page._element.rPr.rFonts.set(qn('w:eastAsia'), '宋体')
run_page.font.size = Pt(14)
run_page._element.append(fldChar1)
run_page._element.append(instrText)
run_page._element.append(fldChar2)

run2 = fp.add_run(' -')
run2.font.name = '宋体'
run2._element.rPr.rFonts.set(qn('w:eastAsia'), '宋体')
run2.font.size = Pt(14)

# 确保没有页眉（删除默认页眉段落）
header = new_section.header
header.is_linked_to_previous = False
header.paragraphs[0].clear()

# ==================== 论文标题页 ====================
center_para(doc, '校园智能路径规划系统', '黑体', 22, bold=True)
empty_para(doc)

# ==================== 摘要 ====================
center_para(doc, '摘    要', '黑体', 16, bold=True)
empty_para(doc)

abstract = (
    '随着高校校园规模的不断扩大，师生在校内的出行需求日益增长。如何快速、准确地规划从起点到终点的最短路径，'
    '成为一个具有实际应用价值的问题。本文设计并实现了一款基于Java的校园智能路径规划系统\u2014\u2014\u201c安心导航\u201d。'
    '该系统以图论为核心理论基础，将校园地理信息建模为带权无向图，实现了Dijkstra最短路径算法和A*启发式搜索算法两种路径规划策略。'
    '系统采用领域驱动设计（DDD）分层架构，包含领域层、应用层、基础设施层和表现层四个层次，结合策略模式、命令模式、MVC模式等'
    '多种设计模式，构建了高内聚、低耦合的软件体系。在数据安全方面，系统实现了基于PBKDF2WithHmacSHA256的密码哈希机制，'
    '保障用户信息安全。在数据持久化方面，系统自研了轻量级JSON解析器，实现了完全离线运行的数据存储方案。'
    '此外，系统还提供了交互式地图编辑、路径可视化、撤销/重做、历史记录等功能，并包含64个单元测试用例以保证代码质量。'
    '实践表明，该系统能够有效解决校园路径规划问题，具有良好的可用性和可扩展性。'
)
body_para(doc, abstract)
empty_para(doc)

p = doc.add_paragraph()
set_line_spacing(p, 28)
set_first_line_indent(p, 28)
run = p.add_run('关键词：路径规划；Dijkstra算法；A*算法；图论；Java Swing；校园导航')
run.font.name = '宋体'
run._element.rPr.rFonts.set(qn('w:eastAsia'), '宋体')
run.font.size = Pt(14)
run.bold = True

empty_para(doc)

# ==================== 一、引言 ====================
heading1_para(doc, '一、引言')
body_para(doc, '随着我国高等教育事业的蓬勃发展，高校校园面积持续扩大，建筑布局日趋复杂。以泰山科技学院为例，校园内分布着教学楼、宿舍楼、食堂、图书馆、运动场等数十栋建筑，师生在日常学习和生活中频繁需要在不同建筑之间往返。传统的纸质地图或经验性认路方式效率低下，特别是在新生入学、访客参观等场景下，精准的路径指引显得尤为重要。')
body_para(doc, '路径规划问题作为计算机科学中的经典问题，在交通导航、物流配送、机器人运动等领域有着广泛应用。在校园场景中，路径规划需要综合考虑道路网络结构、道路类型（主干道、小路、楼梯）、通行限制（施工禁行）等因素。基于此背景，本文设计并实现了\u201c安心导航\u201d\u2014\u2014一款面向校园环境的智能路径规划系统。')
body_para(doc, '该系统以图论为理论基础，将校园地理信息抽象为带权图模型，通过Dijkstra算法和A*算法实现最短路径计算。系统采用Java Swing技术栈构建桌面图形用户界面，支持交互式地图浏览与编辑，为用户提供直观、便捷的导航体验。本文将从系统需求分析、算法设计、系统架构、功能实现、测试验证等方面进行全面阐述。')

empty_para(doc)

# ==================== 二、系统需求分析 ====================
heading1_para(doc, '二、系统需求分析')

heading2_para(doc, '（一）功能需求')
body_para(doc, '1. 路径查询功能：用户可在校园地图上选择起点和终点，系统自动计算最短路径并以可视化方式呈现，同时提供总距离、预计时间、分段导航指引等详细信息。')
body_para(doc, '2. 地图浏览功能：支持地图的缩放、平移操作，用户可自由查看校园全貌或局部细节。不同地点类型以不同颜色和图标标识，增强可读性。')
body_para(doc, '3. 地图编辑功能（管理员）：管理员可对校园地图数据进行维护，包括添加/修改/删除地点和道路、设置临时禁行路段等操作，并支持撤销/重做机制。')
body_para(doc, '4. 用户认证功能：系统区分管理员和普通用户两种角色，管理员拥有地图编辑权限，普通用户仅可进行路径查询。')
body_para(doc, '5. 历史记录功能：自动保存最近20条路径查询记录，方便用户回溯。')
body_para(doc, '6. 数据备份恢复：支持手动创建数据备份和从备份恢复，保障数据安全。')

empty_para(doc)

heading2_para(doc, '（二）非功能需求')
body_para(doc, '1. 性能需求：路径查询响应时间应在1秒以内，地图交互操作（缩放、平移）应流畅无卡顿。')
body_para(doc, '2. 可靠性需求：数据写入采用原子操作，防止写入中断导致数据损坏；支持快照/回滚机制。')
body_para(doc, '3. 安全性需求：用户密码采用PBKDF2WithHmacSHA256哈希存储，不可逆；登录接口实现速率限制，防止暴力破解。')
body_para(doc, '4. 可用性需求：图形界面设计直观友好，符合用户操作习惯；支持完全离线运行，无需网络连接。')
body_para(doc, '5. 可扩展性需求：采用策略模式设计路径规划算法，便于后续扩展新算法；分层架构支持各层独立演进。')

empty_para(doc)

# ==================== 三、核心算法设计 ====================
heading1_para(doc, '三、核心算法设计')

heading2_para(doc, '（一）校园地图的图模型构建')
body_para(doc, '系统将校园地理信息抽象为带权无向图G=(V,E)，其中V为顶点集合，E为边集合。每个顶点代表一个校园地点（如教学楼、宿舍、食堂等），包含唯一标识、名称、类型、坐标等信息；每条边代表两个地点之间的道路，包含起点、终点、距离权重、道路类型、是否禁行等属性。')
body_para(doc, '顶点模型（Vertex）的核心属性包括：id（唯一标识符）、name（地点名称）、type（地点类型枚举，包含教学楼、宿舍、食堂、图书馆、大门、办公室、运动场、其他共八种类型）、x和y（二维坐标，用于地图渲染和启发式距离计算）、description（描述信息）。边模型（Edge）的核心属性包括：fromVertex（起始顶点）、toVertex（目标顶点）、weight（边权重，表示两地点间的实际距离，单位为米）、oneWay（是否单向通行）、forbidden（是否禁行）、roadType（道路类型枚举，包含主干道、小路、楼梯三种）。')
body_para(doc, '图的存储结构采用邻接表（Adjacency List）实现，使用LinkedHashMap维护顶点及其出边列表的映射关系。邻接表相比邻接矩阵在处理稀疏图时具有空间效率优势，校园地图作为典型的稀疏图（44个顶点，边数远小于完全图），邻接表是更为合适的选择。同时，LinkedHashMap保持了顶点的插入顺序，有利于地图数据的确定性遍历。')
body_para(doc, '系统当前数据规模为44个校园地点，涵盖泰山科技学院的主要建筑。地点类型分布为：宿舍12个、教学楼6个、食堂3个、图书馆1个、大门3个、办公室7个、运动场3个、其他类型4个。道路均为双向通行（oneWay=false），道路类型以主干道为主。')

empty_para(doc)

heading2_para(doc, '（二）Dijkstra最短路径算法')
body_para(doc, 'Dijkstra算法由荷兰计算机科学家Edsger W. Dijkstra于1956年提出，是解决非负权图单源最短路径问题的经典算法。本系统实现了基于优先队列优化的Dijkstra算法，具体步骤如下：')
body_para(doc, '步骤1（初始化）：设置起点距离为0，其余所有顶点距离为正无穷大（Double.MAX_VALUE）。创建优先队列（最小堆），将起点及其距离0加入队列。初始化前驱顶点映射表previous和前驱边映射表previousEdge，用于最终路径的回溯重建。')
body_para(doc, '步骤2（主循环）：从优先队列中取出距离最小的顶点u。若u的当前距离大于已记录距离（惰性删除），则跳过。若u恰好为终点，则提前终止循环。遍历u的所有邻接边，对于每条边e=(u\u2192v, w)：若该边被标记为禁行（forbidden=true），则跳过；计算候选距离candidate = dist[u] + w；若candidate < dist[v]，则更新dist[v] = candidate，记录previous[v] = u，previousEdge[v] = e，并将(v, candidate)加入优先队列。')
body_para(doc, '步骤3（路径构建）：若终点不在previous映射中（即从起点无法到达终点），抛出NoRouteFoundException异常。否则，从终点开始沿previous映射逆向回溯到起点，得到逆序路径列表，再通过Collections.reverse()翻转为正向路径。同时收集各段边权重，构建PathResult对象返回。')
body_para(doc, '算法时间复杂度为O((V+E)logV)，其中V为顶点数，E为边数。优先队列的插入和删除操作均为O(logV)，每个顶点最多入队一次、出队一次，每条边最多被松弛一次。算法保证了在非负权图中找到全局最优解（最短路径）。')
body_para(doc, '在实现中，优先队列采用Java标准库的PriorityQueue，通过内部类NodeDistance实现Comparable接口，按distance升序排列。惰性删除策略避免了优先队列中更新元素的高开销：当取出节点的距离大于已记录距离时，说明该节点已被更优路径更新过，直接丢弃即可。')

empty_para(doc)

heading2_para(doc, '（三）A*启发式搜索算法')
body_para(doc, 'A*算法由Peter Hart、Nils Nilsson和Bertram Raphael于1968年提出，是对Dijkstra算法的重要改进。A*算法在Dijkstra的基础上引入了启发式函数h(n)，用于估计当前节点n到目标节点的代价，从而引导搜索方向偏向目标，减少不必要的搜索空间。')
body_para(doc, 'A*算法的核心公式为：f(n) = g(n) + h(n)，其中g(n)为起点到节点n的实际代价（与Dijkstra中的dist相同），h(n)为节点n到终点的启发式估计值，f(n)为节点n的综合评估值。优先队列按f(n)升序排列，每次扩展f(n)最小的节点。')
body_para(doc, '本系统中的启发式函数h(n)采用欧几里得距离（直线距离）：h(n) = sqrt((n.x - goal.x)\u00b2 + (n.y - goal.y)\u00b2)。由于校园地图中的顶点具有二维坐标信息，欧几里得距离是一个天然可用的启发式估计。该启发式函数是可采纳的（admissible），即h(n)永远不会高估实际代价（直线距离\u2264实际路径距离），因此A*算法保证找到最优解。')
body_para(doc, '系统支持可配置的启发式缩放因子scaleFactor（默认值为1.0）。当scaleFactor > 1.0时，启发式权重增大，搜索偏向贪心策略，搜索速度更快但可能牺牲最优性；当scaleFactor = 1.0时，保证最优解。这一设计为用户提供了搜索速度与精度之间的灵活权衡。')
body_para(doc, 'A*算法的实现结构与Dijkstra高度相似，核心差异在于优先队列的排序依据：Dijkstra按g(n)排序，A*按f(n) = g(n) + scaleFactor \u00d7 h(n)排序。通过内部类AstarNode封装顶点ID、gScore和fScore三个字段，实现Comparable接口按fScore升序排列。')

empty_para(doc)

heading2_para(doc, '（四）两种算法的对比分析')
body_para(doc, 'Dijkstra算法和A*算法在理论和实践中各有优劣。Dijkstra算法的优势在于实现简单、不依赖坐标信息、保证最优解；缺点在于搜索范围大，以起点为中心向四周均匀扩展，在顶点数量多时效率较低。A*算法的优势在于利用启发式信息引导搜索方向，在具有坐标信息的场景中搜索效率显著优于Dijkstra；缺点在于需要额外的坐标数据支持，启发式函数的质量直接影响算法性能。')
body_para(doc, '在校园路径规划场景中，由于所有地点均具有明确的二维坐标，A*算法的启发式信息可以充分发挥作用。实际测试表明，对于起点和终点距离较远的查询，A*算法访问的节点数明显少于Dijkstra算法，响应速度更快。系统采用策略模式（Strategy Pattern）封装两种算法，通过PathPlanningStrategy接口统一调用，用户可根据需要灵活切换。')

empty_para(doc)

heading2_para(doc, '（五）图连通性检测算法')
body_para(doc, '系统实现了基于深度优先搜索（DFS）的图连通性检测算法，用于验证地图数据的完整性。算法遍历图中所有顶点，对每个未访问的顶点执行DFS搜索，每次搜索发现一个连通分量。若连通分量数为1，说明图是全连通的；若大于1，则报告所有孤立子图的顶点集合。该功能在地图数据初始化时自动执行，当检测到非连通状态时向管理员发出警告，避免因数据不完整导致部分地点无法到达。')

empty_para(doc)

# ==================== 四、系统架构设计 ====================
heading1_para(doc, '四、系统架构设计')

heading2_para(doc, '（一）整体架构')
body_para(doc, '系统采用领域驱动设计（Domain-Driven Design, DDD）分层架构，从上到下依次为表现层（GUI Layer）、应用层（Application Layer）、领域层（Domain Layer）和基础设施层（Infrastructure Layer）。各层职责明确，依赖方向严格从上到下，上层可依赖下层，下层不感知上层。')
body_para(doc, '表现层（gui包）：负责图形用户界面的构建与交互，采用MVC（Model-View-Controller）模式。视图层包含主窗口MainView、路径查询面板PathQueryView、地图工作台MapWorkbenchView等组件；控制器层包含认证控制器AuthController、导航控制器NavigationController、地图控制器MapController等。地图画布MapCanvas基于Java 2D Graphics2D实现矢量渲染，支持缩放、平移、吸附编辑等交互操作。')
body_para(doc, '应用层（application包）：负责服务编排和用例实现，不包含业务逻辑，仅协调领域对象完成具体任务。包含认证服务AuthService、导航服务NavigationService、地图编辑服务MapService、图连通性检查器GraphConnectivityChecker等。导航服务持有PathPlanningStrategy引用，可在运行时动态切换Dijkstra或A*算法。')
body_para(doc, '领域层（domain包）：包含核心业务逻辑和领域模型，是整个系统的核心。图模块包含CampusGraph图数据结构；模型模块包含Vertex、Edge、PathResult等不可变领域对象；规划模块包含PathPlanningStrategy接口及其Dijkstra和A*两种实现；安全模块包含PasswordHasher密码哈希服务。')
body_para(doc, '基础设施层（infrastructure包）：提供数据持久化支持。PersistenceService负责JSON文件的读写、备份恢复、数据迁移；SimpleJson为自研的轻量级JSON解析器，采用递归下降算法实现，无任何第三方依赖。')

empty_para(doc)

heading2_para(doc, '（二）设计模式应用')
body_para(doc, '1. 策略模式（Strategy Pattern）：路径规划算法的核心设计。定义PathPlanningStrategy接口，DijkstraStrategy和AstarStrategy分别实现该接口。NavigationService持有策略引用，客户端可在运行时切换算法，符合开闭原则（对扩展开放、对修改关闭）。')
body_para(doc, '2. 命令模式（Command Pattern）：地图编辑的撤销/重做机制。定义UndoableCommand接口（含execute/undo/redo方法），每种编辑操作（添加顶点、删除道路、移动地点等）封装为具体命令对象。CommandBus作为命令总线，维护命令历史栈，支持任意深度的撤销和重做。')
body_para(doc, '3. MVC模式：GUI层的标准架构。Controller处理用户输入和业务逻辑调用，Model存储UI状态数据（DTO），View负责界面渲染。三者职责分离，降低耦合度。')
body_para(doc, '4. 工厂模式：DefaultDataFactory在数据文件损坏或缺失时，自动生成包含基本地点和道路的默认数据集，确保系统始终可启动运行。')
body_para(doc, '5. 委托模式：MainView主窗口类将导航状态管理、管理工具栏构建等职责委托给专门的Builder和State类（MainViewLayoutBuilder、MainViewNavigationState、MainViewAdminToolbarBuilder），避免主窗口类过度膨胀。')

empty_para(doc)

heading2_para(doc, '（三）技术栈选择')
body_para(doc, '系统在技术选型上遵循极简原则，核心功能全部基于JDK内置能力实现，仅依赖三个第三方库：SLF4J（日志门面）、Logback（日志实现）、JUnit Jupiter（单元测试框架）。具体技术选型如下：')
body_para(doc, '编程语言选择Java 8，保证了广泛的兼容性和成熟的生态系统。构建工具采用Apache Maven，实现依赖管理和自动化构建。GUI框架采用Java Swing，作为JDK内置的桌面UI工具包，无需额外安装运行时环境，保证了系统的零依赖部署。数据存储采用本地JSON文件，配合自研的SimpleJson解析器，实现了完全离线运行。密码安全基于JCA（Java Cryptography Architecture）的PBKDF2WithHmacSHA256算法，采用12万次迭代的密钥派生，保障用户密码的不可逆存储。图形渲染基于Java 2D API，利用Graphics2D和AffineTransform实现高质量矢量地图渲染。')

empty_para(doc)

# ==================== 五、系统实现 ====================
heading1_para(doc, '五、系统实现')

heading2_para(doc, '（一）核心数据结构实现')
body_para(doc, '图数据结构CampusGraph采用邻接表实现，核心存储结构为两个LinkedHashMap：vertices映射（顶点ID\u2192Vertex对象）和adjList映射（顶点ID\u2192该顶点的出边列表）。边添加时自动处理双向道路：对于非单向边，自动生成反向边加入邻接表。边删除时同步清理对应的反向边。禁行设置时同时对双向边生效。这种对称处理确保了图数据的一致性。')
body_para(doc, '模型类（Vertex、Edge、PathResult）均设计为不可变对象（final class + final字段），通过构造函数进行严格的参数校验（非空检查、数值范围检查），确保领域对象的合法性和线程安全性。不可变设计消除了并发访问的隐患，简化了程序的推理和调试。')

empty_para(doc)

heading2_para(doc, '（二）路径规划策略实现')
body_para(doc, 'PathPlanningStrategy接口定义了统一的规划方法：PathResult plan(CampusGraph graph, String startId, String endId)。该接口将算法与图数据解耦，使得算法实现不依赖于具体的数据来源。NavigationService作为策略的调用者，通过setStrategy()方法支持运行时算法切换，通过getStrategyName()方法获取当前使用的算法名称。')
body_para(doc, '导航结果PathResult不仅包含路径顶点序列和总距离，还通过NavigationService的enrich()方法进行信息富化：根据用户指定的出行速度（步行75 m/min或摆渡车300 m/min）计算预估时间，并生成中文格式的逐步导航指令。导航指令格式为：\u201c第N步：从[地点A]前往[地点B]，步行约XX米。\u201d这种自然语言输出极大提升了用户体验。')

empty_para(doc)

heading2_para(doc, '（三）密码安全实现')
body_para(doc, '系统安全模块基于PBKDF2WithHmacSHA256算法实现密码哈希，关键参数为：迭代次数120,000次、随机盐值16字节（128位）、派生密钥32字节（256位）。哈希结果以结构化字符串存储，格式为：\u201cpbkdf2$iterations$hexSalt$hexHash\u201d，便于解析和版本管理。')
body_para(doc, '密码验证采用常量时间比较（MessageDigest.isEqual()），有效防止时序攻击（Timing Attack）。盐值由SecureRandom密码学安全随机数生成器产生，保证每个用户的盐值独立且不可预测。这些安全措施确保了即使数据库文件泄露，攻击者也难以通过彩虹表或暴力破解方式恢复用户原始密码。')

empty_para(doc)

heading2_para(doc, '（四）数据持久化实现')
body_para(doc, '系统自研了SimpleJson解析器，采用经典的递归下降（Recursive Descent）算法实现JSON的序列化与反序列化。解析器支持完整的JSON语法：对象（{}）、数组（[]）、字符串（含Unicode转义\\uXXXX）、数字（整数、小数、科学计数法）、布尔值（true/false）、null值。内部使用LinkedHashMap维护对象字段顺序，ArrayList存储数组元素，确保解析结果的数据结构语义与JSON原文一致。')
body_para(doc, 'PersistenceService封装了数据持久化的完整流程。数据写入采用原子操作：先将数据写入临时文件（.tmp后缀），写入完成后通过Files.move()以ATOMIC_MOVE选项原子性地替换目标文件。这种设计防止了写入过程中系统崩溃导致的数据文件损坏。系统还维护数据版本号（DATA_VERSION=2），支持数据格式的平滑迁移。备份功能将当前数据完整复制到带时间戳的备份目录，恢复功能则从指定备份还原。')

empty_para(doc)

heading2_para(doc, '（五）地图可视化实现')
body_para(doc, '地图画布MapCanvas基于Java 2D Graphics2D实现，采用分层渲染架构：BaseSceneRenderer负责网格背景和基础道路绘制，RouteRenderer负责规划路径的高亮渲染，OverlayRenderer负责选择状态、编辑引导线等交互元素的渲染。每层独立渲染，互不干扰。')
body_para(doc, '缩放功能通过AffineTransform变换实现，支持鼠标滚轮缩放和拖拽平移。坐标系统采用世界坐标（与顶点数据的x/y对应），通过视图变换矩阵转换为屏幕坐标。吸附引擎（SnapEngine）在编辑模式下自动检测鼠标位置附近的顶点，提供编辑引导，提升操作精度。')

empty_para(doc)

heading2_para(doc, '（六）认证与权限实现')
body_para(doc, '认证服务AuthService实现了用户登录的完整流程：验证用户名是否存在、验证密码哈希是否匹配、检查账号锁定状态。系统内置两个默认账号：admin/admin123（管理员角色）和guest/guest123（访客角色）。管理员拥有地图编辑的全部权限（增删改地点/道路、设置禁行），普通用户仅可进行路径查询和地图浏览。')
body_para(doc, '登录安全方面，系统实现了基于计数的速率限制机制：同一用户名连续5次登录失败后，账号被锁定30秒。锁定期间即使密码正确也拒绝登录，有效防止暴力破解攻击。权限控制通过MapService中的requireAdmin()方法实现，在每次地图编辑操作前检查操作者是否具有管理员角色。')

empty_para(doc)

# ==================== 六、测试与验证 ====================
heading1_para(doc, '六、测试与验证')

heading2_para(doc, '（一）单元测试')
body_para(doc, '系统包含20个JUnit 5测试类，共计64个测试用例，覆盖了所有核心模块。测试模块分布如下：')
body_para(doc, '1. 图数据结构测试：验证CampusGraph的顶点/边增删改查、邻接表正确性、双向边处理逻辑。测试用例包括添加重复顶点、删除不存在顶点、添加自环边等边界情况。')
body_para(doc, '2. 路径规划算法测试：验证Dijkstra算法和A*算法在不同图结构下的正确性。测试场景包括：简单路径（2个顶点1条边）、多路径选择（确保选最短）、不可达起点终点（验证异常抛出）、禁行边过滤（验证禁行边被正确跳过）。')
body_para(doc, '3. 密码安全测试：验证PasswordHasher的哈希生成与验证一致性、不同密码产生不同哈希值、格式解析正确性、异常输入处理。')
body_para(doc, '4. 认证服务测试：验证AuthService的正常登录流程、错误密码拒绝、角色权限检查、速率限制机制的正确触发和恢复。')
body_para(doc, '5. 地图服务测试：验证MapService的CRUD操作完整性、权限检查（非管理员操作拒绝）、数据一致性维护。')
body_para(doc, '6. 导航服务测试：验证NavigationService的路径富化逻辑、导航指令生成格式、速度参数对预估时间的影响。')
body_para(doc, '7. JSON解析测试：验证SimpleJson的序列化与反序列化双向转换一致性、特殊字符处理、嵌套结构解析、大数据量处理性能。')
body_para(doc, '8. 持久化测试：验证PersistenceService的读写一致性、原子写入机制、备份恢复流程、数据迁移逻辑。')
body_para(doc, '9. 连通性测试：验证GraphConnectivityChecker对不同连通状态的正确判断，包括全连通图、含孤立顶点图、空图等场景。')
body_para(doc, '所有64个测试用例均通过，未发现功能缺陷。测试采用AAA（Arrange-Act-Assert）模式编写，测试方法命名遵循should_ExpectedBehavior_When_StateUnderTest约定，提高了测试代码的可读性和可维护性。')

empty_para(doc)

heading2_para(doc, '（二）功能验证')
body_para(doc, '在功能层面，通过实际运行系统进行了以下验证：路径查询功能正确返回最短路径，在地图上以醒目颜色高亮显示；地图缩放和平移操作流畅无卡顿；管理员地图编辑功能（添加/删除地点和道路、设置禁行）正常工作，撤销/重做功能可恢复任意步操作；用户认证流程完整，角色权限隔离有效；历史记录自动保存并可回溯；数据备份和恢复功能可靠。')

empty_para(doc)

# ==================== 七、总结与展望 ====================
heading1_para(doc, '七、总结与展望')

heading2_para(doc, '（一）工作总结')
body_para(doc, '本文设计并实现了一款基于Java的校园智能路径规划系统\u2014\u2014安心导航。系统以图论为核心理论基础，实现了Dijkstra和A*两种经典最短路径算法，采用DDD分层架构和多种设计模式构建了结构清晰、易于维护的软件体系。系统的主要成果包括：')
body_para(doc, '1. 完整的路径规划功能：支持Dijkstra和A*两种算法，用户可根据场景灵活切换，路径结果包含总距离、预估时间、分段导航指引等丰富信息。')
body_para(doc, '2. 直观的可视化地图：基于Java 2D实现矢量地图渲染，支持缩放、平移、交互编辑，不同地点类型以不同颜色标识。')
body_para(doc, '3. 可靠的数据管理：自研JSON解析器实现完全离线数据存储，原子写入保障数据安全，备份恢复机制增强数据可靠性。')
body_para(doc, '4. 安全的用户体系：PBKDF2密码哈希、速率限制、角色权限隔离，构建了基础的安全防护体系。')
body_para(doc, '5. 充分的测试覆盖：64个单元测试用例覆盖所有核心模块，确保代码质量和功能正确性。')

empty_para(doc)

heading2_para(doc, '（二）不足与展望')
body_para(doc, '尽管系统实现了预期的核心功能，但仍存在以下可改进之处：')
body_para(doc, '1. 算法扩展：当前仅实现了Dijkstra和A*两种算法，后续可引入Floyd-Warshall全源最短路径算法、Bellman-Ford算法（支持负权边）、双向搜索优化等。')
body_para(doc, '2. 实时路况：当前系统为静态路径规划，未考虑实时路况信息（如临时活动封路、人流密度等），后续可引入动态权重调整机制。')
body_para(doc, '3. 多目标路径：当前仅支持单起点到单终点的路径规划，后续可扩展为多途经点路径规划（类似旅行商问题TSP的近似求解）。')
body_para(doc, '4. 移动端适配：当前为Java桌面应用，后续可考虑开发Web版本或移动端版本，扩大用户覆盖范围。')
body_para(doc, '5. 地图数据丰富：当前地图数据规模为44个地点，后续可补充更多校园细节（如停车场、ATM、快递点等），提升实用性。')
body_para(doc, '6. 无障碍导航：可引入针对行动不便人士的无障碍路径规划（避开楼梯、优先选择坡道等），体现人文关怀。')

empty_para(doc)

# ==================== 参考文献 ====================
heading1_para(doc, '参考文献')
empty_para(doc)

refs = [
    '[1] Dijkstra E W. A note on two problems in connexion with graphs[J]. Numerische Mathematik, 1959, 1(1): 269-271.',
    '[2] Hart P E, Nilsson N J, Raphael B. A formal basis for the heuristic determination of minimum cost paths[J]. IEEE Transactions on Systems Science and Cybernetics, 1968, 4(2): 100-107.',
    '[3] Cormen T H, Leiserson C E, Rivest R L, et al. 算法导论[M]. 殷建平, 徐云, 王刚, 等译. 北京: 机械工业出版社, 2013.',
    '[4] Gamma E, Helm R, Johnson R, et al. 设计模式: 可复用面向对象软件的基础[M]. 李英军, 马晓星, 蔡敏, 等译. 北京: 机械工业出版社, 2000.',
    '[5] Evans E. 领域驱动设计: 软件核心复杂性应对之道[M]. 赵俐, 盛海艳, 刘霞, 等译. 北京: 人民邮电出版社, 2016.',
    '[6] Oracle. Java Cryptography Architecture (JCA) Reference Guide[EB/OL]. https://docs.oracle.com/javase/8/docs/technotes/guides/security/crypto/CryptoSpec.html, 2024.',
    '[7] Sedgewick R, Wayne K. 算法(第4版)[M]. 谢路云, 译. 北京: 人民邮电出版社, 2012.',
]
for ref in refs:
    body_para(doc, ref)

# ==================== 保存文档 ====================
output_path = r'd:\22523\anxin-main\10.《算法课程设计》泰山科技学院课程考核论文新.docx'
doc.save(output_path)
print(f'论文生成成功: {output_path}')
