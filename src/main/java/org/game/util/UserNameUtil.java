package org.game.util;

import java.util.LinkedList;
import java.util.List;


public class UserNameUtil {

    private static String[] firstNames = {
            "赵", "钱", "孙", "李", "周", "吴", "郑", "王", "冯", "陈", "褚", "卫", "蒋", "沈", "韩", "杨", "朱", "秦", "尤", "许", "何", "吕", "施","张","孔",
            "曹", "严", "华", "金", "魏", "陶", "姜", "戚", "谢", "邹", "喻", "柏", "水", "窦", "章", "云", "苏", "潘", "葛", "奚", "范", "彭", "郎","鲁","韦",
            "昌", "马", "苗", "凤", "花", "方", "俞", "任", "袁", "柳", "酆", "鲍", "史", "唐", "费", "廉", "岑", "薛", "雷", "贺", "倪", "汤", "滕","殷","应",
            "罗", "毕", "郝", "邬", "安", "常", "乐", "于", "时", "傅", "皮", "卞", "齐", "康", "伍", "余", "元", "卜", "顾", "孟", "平", "黄", "和","舒","山",
            "罗", "毕", "郝", "邬", "安", "常", "乐", "于", "时", "傅", "皮", "卞", "齐", "康", "伍", "余", "元", "卜", "顾", "孟", "平", "黄", "和","钟","荀",
            "穆", "萧", "尹", "姚", "邵", "湛", "汪", "祁", "毛", "禹", "狄", "米", "贝", "明", "臧", "计", "伏", "成", "戴", "谈", "宋", "茅", "庞","熊","纪",
            "屈", "项", "祝", "董", "梁", "杜", "阮", "蓝", "闵", "席", "季", "麻", "强", "贾", "路", "娄", "危", "江", "童", "颜", "郭", "梅", "盛","林","刁",
            "徐", "邱", "骆", "高", "夏", "蔡", "田", "樊", "胡", "凌", "霍", "虞", "万", "支", "柯", "昝", "管", "卢", "莫", "经", "房", "裘", "缪","干","解",
            "宗", "丁", "宣", "贲", "邓", "郁", "单", "杭", "洪", "包", "诸", "左", "石", "崔", "吉", "钮", "龚", "程", "嵇", "邢", "滑", "裴", "陆","荣","翁",
            "羊", "于", "惠", "甄", "曲", "家", "封", "芮", "羿", "储", "靳", "汲", "邴", "糜", "松", "井", "段", "富", "巫", "乌", "焦", "巴", "弓","牧","隗",
            "谷", "车", "侯", "宓", "蓬", "全", "郗", "班", "仰", "秋", "仲", "伊", "宫", "宁", "仇", "栾", "暴", "甘", "钭", "厉", "戎", "祖", "武","符","刘",
            "詹", "束", "龙", "叶", "幸", "司", "韶", "郜", "黎", "蓟", "溥", "印", "宿", "白", "怀", "蒲", "邰", "从", "鄂", "索", "咸", "籍", "赖","卓","蔺",
            "蒙", "池", "乔", "阴", "郁", "胥", "能", "苍", "双", "闻", "莘", "党", "翟", "谭", "贡", "劳", "逄", "姬", "申", "扶", "堵", "冉", "宰", "郦", "雍",
            "璩", "桑", "桂", "濮", "牛", "寿", "通", "边", "扈", "燕", "冀", "浦", "尚", "农", "温", "别", "庄", "晏", "柴", "瞿", "阎", "充", "慕", "连", "茹",
            "宦", "艾", "鱼", "容", "向", "古", "易", "慎", "戈", "廖", "庾", "终", "暨", "居", "衡", "步", "都", "耿", "满", "弘", "匡", "国", "文", "寇", "广",
            "阙", "东", "欧", "殳", "沃", "利", "蔚", "越", "夔", "隆", "师", "巩", "厍", "聂", "晁", "勾", "敖", "融", "冷", "訾", "辛", "阚", "那", "简", "饶",
            "曾", "毋", "沙", "乜", "养", "鞠", "须", "丰", "巢", "关", "蒯", "相", "查", "后", "荆", "红", "游", "郏", "竺", "权", "逯", "盖", "益", "桓", "公",
            "督", "岳", "帅", "缑", "亢", "况", "郈", "有", "琴", "归", "海", "晋", "楚", "闫", "法", "汝", "鄢", "涂", "钦", "商", "牟", "佘", "佴", "伯", "赏",
            "哈", "谯", "篁", "年", "爱", "阳", "佟", "言", "福", "南", "火", "铁", "迟", "漆", "官", "冼", "真", "展", "繁", "檀", "祭", "密", "敬", "揭", "舜",
            "疏", "冒", "浑", "挚", "胶", "随", "高", "皋", "原", "种", "练", "弥", "仓", "眭", "蹇", "覃", "阿", "门", "恽", "来", "綦", "召", "仪", "风", "介",
            "木", "京", "狐", "郇", "虎", "枚", "抗", "达", "杞", "苌", "折", "麦", "庆", "过", "竹", "端", "鲜", "皇", "亓", "老", "是", "秘", "畅", "邝", "还",
            "宾", "a", "b", "c", "d", "e", "f", "g", "h", "i", "c", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z",
            "A", "B", "C", "D", "E", "F", "G", "H", "I", "C", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"
    };

    public static String[] lastNames = {
            "生活", "一起", "不是", "人们", "今天", "孩子", "心里", "奶奶", "眼睛",
            "学校", "原来", "爷爷", "地方", "过去", "事情", "以后", "可能", "晚上",
            "里面", "才能", "知识", "故事", "多少", "比赛", "冬天", "所有", "样子",
            "成绩", "后来", "以前", "童年", "问题", "日子", "活动", "公园", "作文",
            "旁边", "下午", "自然", "房间", "空气", "笑容", "明天", "风景", "音乐",
            "岁月", "文化", "生气", "机会", "身影", "天气", "空中", "红色", "书包",
            "今年", "汽车", "早晨", "道路", "认识", "办法", "精彩",
            "中午", "礼物", "星星", "习惯", "树木", "女儿", "友谊", "夜晚", "意义",
            "家长", "耳朵", "家人", "门口", "班级", "人间", "厨房", "风雨", "影响",
            "过年", "电话", "黄色", "种子", "广场", "清晨", "根本", "故乡", "笑脸",
            "水面", "思想", "伙伴", "美景", "照片", "水果", "彩虹", "刚才", "月光",
            "先生", "鲜花", "灯光", "感情", "亲人", "语言", "爱心", "光明", "左右",
            "新年", "角落", "青蛙", "电影", "行为", "阳台", "用心", "题目", "天地",
            "动力", "花园", "诗人", "树林", "雨伞", "去年", "少女", "乡村", "对手",
            "上午", "分别", "活力", "作用", "古代", "公主", "力气", "从前", "作品",
            "空间", "黑夜", "说明", "青年", "面包", "往事", "大小", "司机",
            "中心", "对面", "心头", "嘴角", "家门", "书本", "雪人", "笑话",
            "云朵", "早饭", "右手", "水平", "行人", "乐园", "花草", "人才", "左手",
            "目的", "课文", "优点", "灰尘", "年代", "沙子", "小说", "儿女", "明星",
            "难题", "本子", "水珠", "彩色", "路灯", "把握", "房屋", "心愿", "左边",
            "新闻", "早点", "市场", "雨点", "细雨", "书房", "毛巾", "画家", "元旦",
            "绿豆", "本领", "起点", "青菜", "土豆", "总结", "礼貌", "右边", "窗帘",
            "萝卜", "深情", "楼房", "对话", "面条", "北方", "木头", "商店", "疑问",
            "后果", "现场", "诗词", "光亮", "白菜", "男子", "风格", "大道", "梦乡",
            "姐妹", "毛衣", "园丁", "两边", "大风", "乡下", "广播", "规定", "围巾",
            "意见", "大方", "头脑", "老大", "成语", "专业", "背景", "大衣", "黄豆",
            "高手", "叶片", "过往", "选手", "奶油", "时空", "大气", "借口", "抹布",
            "画笔", "山羊", "晚会", "拖鞋", "手心", "手工", "明年", "手术", "火苗",
            "知己", "价格", "树苗", "主意", "摇篮", "对比", "胖子", "专家", "年级",
            "难题", "本子", "水珠", "彩色", "路灯", "把握", "房屋", "心愿", "左边",
            "新闻", "早点", "市场", "雨点", "细雨", "书房", "毛巾", "画家", "元旦",
            "绿豆", "本领", "起点", "青菜", "土豆", "总结", "礼貌", "右边", "窗帘",
            "萝卜", "深情", "楼房", "对话", "面条", "北方", "木头", "商店", "疑问",
            "后果", "现场", "诗词", "光亮", "白菜", "男子", "风格", "大道", "梦乡",
            "姐妹", "毛衣", "园丁", "两边", "大风", "乡下", "广播", "规定", "围巾",
            "意见", "大方", "头脑", "老大", "成语", "专业", "背景", "大衣", "黄豆",
            "高手", "叶片", "过往", "选手", "奶油", "时空", "大气", "借口", "抹布",
            "画笔", "山羊", "晚会", "拖鞋", "手心", "手工", "明年", "手术", "火苗",
            "知己", "价格", "树苗", "主意", "摇篮", "对比", "胖子", "专家", "年级",
            "落日", "东风", "屋子", "创意", "报道", "下巴", "面子", "迷宫", "雪山",
            "友好", "烟雾", "西方", "姨妈", "问号", "年轮", "居民", "选手", "奶油",
            "时空", "大气", "借口", "抹布", "画笔", "山羊", "晚会", "拖鞋", "手心",
            "手工", "明年", "手术", "火苗", "知己", "价格", "树苗", "主意", "摇篮",
            "对比", "胖子", "专家", "年级", "落日", "东风", "屋子", "创意", "报道",
            "下巴", "面子", "迷宫", "雪山", "友好", "烟雾", "西方", "姨妈", "问号",
            "年轮", "居民", "选手", "奶油", "时空", "大气", "借口", "抹布", "画笔",
            "山羊", "晚会", "拖鞋", "手心", "手工", "明年", "手术", "火苗", "知己",
            "屈", "项", "祝", "董", "梁", "杜", "阮", "蓝", "闵", "席", "季", "麻", "强", "贾", "路", "娄", "危", "江", "童", "颜", "郭", "梅", "盛","林","刁",
            "徐", "邱", "骆", "高", "夏", "蔡", "田", "樊", "胡", "凌", "霍", "虞", "万", "支", "柯", "昝", "管", "卢", "莫", "经", "房", "裘", "缪","干","解",
            "宗", "丁", "宣", "贲", "邓", "郁", "单", "杭", "洪", "包", "诸", "左", "石", "崔", "吉", "钮", "龚", "程", "嵇", "邢", "滑", "裴", "陆","荣","翁",
            "羊", "于", "惠", "甄", "曲", "家", "封", "芮", "羿", "储", "靳", "汲", "邴", "糜", "松", "井", "段", "富", "巫", "乌", "焦", "巴", "弓","牧","隗",
            "谷", "车", "侯", "宓", "蓬", "全", "郗", "班", "仰", "秋", "仲", "伊", "宫", "宁", "仇", "栾", "暴", "甘", "钭", "厉", "戎", "祖", "武","符","刘",
            "詹", "束", "龙", "叶", "幸", "司", "韶", "郜", "黎", "蓟", "溥", "印", "宿", "白", "怀", "蒲", "邰", "从", "鄂", "索", "咸", "籍", "赖","卓","蔺",
            "蒙", "池", "乔", "阴", "郁", "胥", "能", "苍", "双", "闻", "莘", "党", "翟", "谭", "贡", "劳", "逄", "姬", "申", "扶", "堵", "冉", "宰", "郦", "雍",
            "璩", "桑", "桂", "濮", "牛", "寿", "通", "边", "扈", "燕", "冀", "浦", "尚", "农", "温", "别", "庄", "晏", "柴", "瞿", "阎", "充", "慕", "连", "茹",
            "宦", "艾", "鱼", "容", "向", "古", "易", "慎", "戈", "廖", "庾", "终", "暨", "居", "衡", "步", "都", "耿", "满", "弘", "匡", "国", "文", "寇", "广",
            "阙", "东", "欧", "殳", "沃", "利", "蔚", "越", "夔", "隆", "师", "巩", "厍", "聂", "晁", "勾", "敖", "融", "冷", "訾", "辛", "阚", "那", "简", "饶",
            "曾", "毋", "沙", "乜", "养", "鞠", "须", "丰", "巢", "关", "蒯", "相", "查", "后", "荆", "红", "游", "郏", "竺", "权", "逯", "盖", "益", "桓", "公",
            "督", "岳", "帅", "缑", "亢", "况", "郈", "有", "琴", "归", "海", "晋", "楚", "闫", "法", "汝", "鄢", "涂", "钦", "商", "牟", "佘", "佴", "伯", "赏",
            "哈", "谯", "篁", "年", "爱", "阳", "佟", "言", "福", "南", "火", "铁", "迟", "漆", "官", "冼", "真", "展", "繁", "檀", "祭", "密", "敬", "揭", "舜",
            "疏", "冒", "浑", "挚", "胶", "随", "高", "皋", "原", "种", "练", "弥", "仓", "眭", "蹇", "覃", "阿", "门", "恽", "来", "綦", "召", "仪", "风", "介",
            "木", "京", "狐", "郇", "虎", "枚", "抗", "达", "杞", "苌", "折", "麦", "庆", "过", "竹", "端", "鲜",
            "价格", "树苗", "主意", "摇篮", "对比", "胖子", "专家", "年级", "落日",
            "东风", "屋子", "创意", "报道", "下巴", "面子", "迷宫", "雪山", "友好",
            "烟雾", "西方", "姨妈", "问号", "年轮", "居民","a", "b", "c", "d", "e", "f", "g", "h", "i", "c", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z",
            "A", "B", "C", "D", "E", "F", "G", "H", "I", "C", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z","一","乙","二","十","丁","厂","七","卜","人","入","八","九","几","儿","了","力","乃","刀","又",
            "三","于","干","亏","士","工","土","才","寸","下","大","丈","与","万","上","小","口","巾","山",
            "千","乞","川","亿","个","勺","久","凡","及","夕","丸","么","广","亡","门","义","之","尸","弓",
            "己","已","子","卫","也","女","飞","刃","习","叉","马","乡","丰","王","井","开","夫","天","无",
            "元","专","云","扎","艺","木","五","支","厅","不","太","犬","区","历","尤","友","匹","车","巨",
            "牙","屯","比","互","切","瓦","止","少","日","中","冈","贝","内","水","见","午","牛","手","毛",
            "气","升","长","仁","什","片","仆","化","仇","币","仍","仅","斤","爪","反","介","父","从","今",
            "凶","分","乏","公","仓","月","氏","勿","欠","风","丹","匀","乌","凤","勾","文","六","方","火",
            "为","斗","忆","订","计","户","认","心","尺","引","丑","巴","孔","队","办","以","允","予","劝",
            "双","书","幻","玉","刊","示","末","未","击","打","巧","正","扑","扒","功","扔","去","甘","世",
            "古","节","本","术","可","丙","左","厉","右","石","布","龙","平","灭","轧","东","卡","北","占",
            "业","旧","帅","归","且","旦","目","叶","甲","申","叮","电","号","田","由","史","只","央","兄",
            "叼","叫","另","叨","叹","四","生","失","禾","丘","付","仗","代","仙","们","仪","白","仔","他",
            "斥","瓜","乎","丛","令","用","甩","印","乐","句","匆","册","犯","外","处","冬","鸟","务","包",
            "饥","主","市","立","闪","兰","半","汁","汇","头","汉","宁","穴","它","讨","写","让","礼","训",
            "必","议","讯","记","永","司","尼","民","出","辽","奶","奴","加","召","皮","边","发","孕","圣",
            "对","台","矛","纠","母","幼","丝","式","刑","动","扛","寺","吉","扣","考","托","老","执","巩",
            "圾","扩","扫","地","扬","场","耳","共","芒","亚","芝","朽","朴","机","权","过","臣","再","协",
            "西","压","厌","在","有","百","存","而","页","匠","夸","夺","灰","达","列","死","成","夹","轨",
            "邪","划","迈","毕","至","此","贞","师","尘","尖","劣","光","当","早","吐","吓","虫","曲","团",
            "同","吊","吃","因","吸","吗","屿","帆","岁","回","岂","刚","则","肉","网","年","朱","先","丢",
            "舌","竹","迁","乔","伟","传","乒","乓","休","伍","伏","优","伐","延","件","任","伤","价","份",
            "华","仰","仿","伙","伪","自","血","向","似","后","行","舟","全","会","杀","合","兆","企","众",
            "爷","伞","创","肌","朵","杂","危","旬","旨","负","各","名","多","争","色","壮","冲","冰","庄",
            "庆","亦","刘","齐","交","次","衣","产","决","充","妄","闭","问","闯","羊","并","关","米","灯",
            "州","汗","污","江","池","汤","忙","兴","宇","守","宅","字","安","讲","军","许","论","农","讽",
            "设","访","寻","那","迅","尽","导","异","孙","阵","阳","收","阶","阴","防","奸","如","妇","好",
            "她","妈","戏","羽","观","欢","买","红","纤","级","约","纪","驰","巡","寿","弄","麦","形","进",
            "戒","吞","远","违","运","扶","抚","坛","技","坏","扰","拒","找","批","扯","址","走","抄","坝"
    };

    private static String[] names ={
            "沃z子","艾划","满书房","齐a远","门乓","窦传","Q作文","牛as空中","堵ds讯","越sd优","江大ds气","爱水平","桑行人","桑毛巾","那后",
            "沃孩子","艾sd划","满书as房","齐da远","门d乓","窦传da","Q作d文","牛空中","堵讯sd","越优s","江大dsa气","爱水asd平","桑行faa人","桑a毛巾","那da后",
            "a孩子","艾d划","满书s房","齐a远","门s乓","窦sd传","Q作sd文","牛空sd中","堵讯a","越优a","江大aa气","爱sdf水平","桑s行人","桑毛d巾","那后a",
            "奚自","濮月光","崔水珠","费代","裘摇篮","卫华","勾灯光","过讯","狐一起","祖飞","耿巡","戴礼貌","佘雪山","p后来","薛口",
            "岳屋子","秦G","储土豆","孙麦","强奶","胥后果","庄面条","闵家人","宁屿","劳西方","K四","经诗人","须兄","纪屋子","H忙",
            "虞仍","岑宁","綦年轮","米雪山","刁行人","F拒","匡尼","季川","邬画笔","黄白","召门","尹水平","胥兄","裘画笔","丰失","毕木头",
            "罗切","甄对","籍目的","康水珠","牛寿","冉山羊","弘对比","挚窗帘","陆抹布","钦壮","郗老大","余乒","印下巴","枚协","傅P",
            "伯灯","耿树林","金米","鄢生活","鲁作品","鲁成语","陆胖子","牛占","元习","翟价格","狄运","w羊","杜尖","毋目的","郁乏",
            "康尤","终丹","钭爱心","蓬l","仲问题","充失","华出","储屯","杨左右","潘西方","伊手","郇打","宿明年","折心里","C奶油",
            "居奸","申时空","戎任","晁纠","宰明年","师原来","朱止","常目","彭火苗","齐借口","隆明年","暨于","冷麦","h司","项化",
            "支币","火匀","封平","黎冬天","骆价格","傅手工","杜西","昌戏","郈后果","毕生活","邵批","和公主","殳勾","程画家","东多",
            "易青年","昌尖","陈万","溥叶片","密大衣","耿辽","o形","饶允","能下午","晋种子","溥尸","支青蛙","巩面子","柏爱心","时且",
            "x闯","梅空气","狐火苗","侯讽","帅订","邢后果","钮奴","羿用","熊抚","文分别","章万","谈山羊","苏厌","dT","Q让","蹇云",
            "邱烟雾","解伞","宰企","杨队","干大","冯手心","章月","挚吐","谭东","凤扰","富同","项支","滑早晨","齐寺","胡心","G女儿",
            "皮贝","俞面包","k疑问","崔寿","南胖子","麦抹布","牟土豆","年任","V于","訾孩子","计氏","汝乒","戴先","随专家","法价格",
            "储孩子","松列","怀时空","蓬华","荀血","邰王","莘烟雾","狐摇篮","赖百","门衣","惠匆","曹防","籍女儿","鞠从","邴价格",
            "喻明天","糜民","松夕","乐必","干由","段共","钦落日","逯扔","况现场","项广","沈后","辛过年","项负","郭事情","后众","姚刚",
            "农云朵","范尖","牧如","双空间","亢并","房乃","D匹","通s","步种子","元毕","伍心头","董勾","黄专家","劳刚才","翟加","暨天",
            "山旨","卓闯","凤明年","郎匠","钱术","曲画笔","阎专业","郜姨妈","蹇厂","武年级","欧奸","惠问号","何迅","法光","强右","W爷",
            "盖白","容犬","许贝","狐厂","京成语","O样子","郝运","萧尤","卓夜晚","瞿丹","齐树木","庾奶油","艾大道","郝扯","贡号","C红色",
            "李小","L故乡","符礼物","郑帅","鲍厉","糜未","q犯","董十","钭天地","尹讨","勾背景","石早饭","江丝","篁务","别册","贾执",
            "端园丁","沈晚会","杜v","顾负","汤以后","仪色","居田","靳乐园","许目的","元现场","火机","苍汽车","o笑容","桑支","郭明天",
            "T新闻","亓房间","鄢树苗","海知己","段朴","喻过","P手工","Q光亮","纪礼","仇产","B互","Up","白以前","厉晚会","印老大","时礼貌",
            "宿丛","姚仿","班久","郈刚才","浑本领","索贞","安司机","逄k","仲去年","容扣","红七","贝年级","汝冲","孟吓","罗叶片","湛升",
            "岳以后","萧说明","甘权","仓黑夜","容吞","裴书本","R伤","田邪","朱产","郗明天","苗明星","充云朵","程叨","须叼","冒从","f欢",
            "骆访","C术","庾厂","柴虫","蔡y","饶友好","刘往事","达亡","卢夫","廖R","郗吃","骆比","缪奸","竹迅","a明年","黄坝","游十","e军",
            "繁毕","蒙卡","房论","q饥","D乃","孟尸","巢云","展己","钭今年","亓讯","喻千","D屯","老巧","窦扶","车今年","窦老","富犬","万手心",
            "篁行","殳年轮","胶下巴","叶岁","苌旨","秦吸","牛友谊","方语言","高手工","滑雪山","福予","昝c","田伞","桂乡","翟M","强年级",
            "终手工","抗高手","于今","糜岁月","羿讲","祭观","湛亦","夏年轮","g风雨","茅旧","莫北","黎空气","蒯屿","关对比","仲水平","毕刑",
            "戚件","家好","Ql","蒙邪","鄂凤","奶油齐问","种红色","戴a","南尤","管W","季号","关纠","裘古代","安奶奶","阮电","沈万","褚年代",
            "琴观","高天地","黎火苗","窦朵","常动","刁拖鞋","郗了","茅共","满民","林北","高迈","璩弄","余空中","冷冰","司仔","年犯","郜甩",
            "充问题","宿考","we","鲜丙","N窗帘","吴以","广太","管朵","贲伙","谭现场","木升","麻伤","卢大道","易刀","翟礼貌","栾原来","密名",
            "浑庄","弥壮","夔A","闫笑脸","杭未","步拖鞋","鄂议","茅延","滕沙子","李手心","还印","纪晚会","牟吗","E历","和晚会","毕次","水早晨",
            "钱市场","扈刊","逄土豆","冉她","蹇民","逄光","边对比","g吉","孙过","贝闪","厍奶油","归五","余乡下","经年","陈仅","何仍","朱后果",
            "祖习惯","扈斥","q西方","Y两边","勾西方","于已","宓i","狐氏","e于","C下巴","余老","伏并","况丙","皇故乡","冒甩","y山","真冲","秋选手",
            "牛音乐","厉奶奶","俞反","寇乐园","Z主意","糜级","许汤","祭八","从黄色","焦字","繁吓","逯光亮","周亲人","顾可","和水","池讯","钟奶奶",
            "蔡了","康同","涂优","竹青年","曾k","南手","向产","邱幼","t托","班会","昌厂","籍房屋","n协","皮伟","u协","余帅","姚分别","仪才能",
            "颜百","k级","仰闪","浦设","毋下巴","吴拖鞋","卓及","缪井","揭兆","涂作品","宋把握","黎青菜","鞠办","殳划","傅工","易云朵","邰雨伞",
            "邹样子","溥奶油","欧规定","郎名","毛史","商红色","程无","范E","支知己","狄仆","佘山羊","赵齐","卜新闻","牧设","童引","富难题","晁纪",
            "沃扎","w语言","毕气","巴水面","风大气","谭n","鱼创意","童过去","禹争","尚岂","O本","蓝西方","余诗词","郭题目","文形","焦句","曲尖",
            "糜地","丰面子","宰正","孟才","通下午","汤又","酆借口","杭戏","刘亡","亓风","史后果","张予","y尼","谭仇","佟大风","E手术","N圾","蒲比",
            "游c","许匆","伏妈","经凶","阎轨","师g","g三","楚抹布","饶发","奶油包阶","毛舌","洪幼","臧议","皮手术","越臣","龚百","宋手术","安手",
            "弓年轮","杨门","q化","终穴","聂权","麦江","闫只","闻习惯","xz","邰树苗","左予","冀企","言虫","楚H","胥心","伍白菜","鄂兄","蒯匠",
            "梅立","米式","颜年轮","贺技","D童年","曲广播","东年轮","党路灯","温冈","q灰尘","步申","篁迁","窦寿","栾去","利商店","祭异","庞F",
            "马叮","费面子","郝诗词","原人间","姜介","余去年","阮份","蔡把握","杨创意","逯水","皮去年","周凶","康戒","时心","水无","滕晚上",
            "晋左边","范尖","班导","慎式","桑旧","狐抹布","S摇篮","滕生活","惠讯","闵比","冀无","狐下巴","红画笔","D右","祭优点","潘夺","p龙",
            "B出","禹月","蔺杂","晋三","柳o","蒲外","厍北","骆烟雾","冒汗","孟报道","o岁月","密礼物","卞旦","闻习","山夹","C树苗","高选手","过七",
            "仇少","颜笑脸","禹伍","g班级","卜大方","湛面子","真尽","元存","蓟v","x大小","蔡好","卞孩子","寿甲","郜女","黄以后","高选手","薛抄",
            "侯行","庾夹","关友谊","蓬四","浑老","索必","陶拖鞋","乜e","昌云","q回","余清晨","宋山","鱼乡下","陶戏","u半","家帅","凌门","寿S",
            "d乎","漆G","M内","冒庆","娄因","匡上","别天地","h友","严影响","海份","庾友好","伍尺","万虫","苗H","井d","许手心","谷且","平麦",
            "舜里面","毕丛","晋灰","毕奴","a音乐","权全","国办","凤丢","戎扩","童约","于山","酆火苗","韩北方","R夹","滑拒","欧从","d毛巾",
            "康牙","任句","伍居民","芮讽","郎叼","滕代","支机","顾乓","廉对比","韶凶","宦对","蒙写","靳仰","满居民","丰场","杨总结","刁仍",
            "包绿豆","姜先","鱼右边","佘劣","萧千","向Q","荣主","于友好","闵早","蒙女","孔军","麻尽","高以","r及","逯落日","汪先生","虞访",
            "印再","Q尤","宰介","郑考","沙九","翟多少","钱奶油","V乙","l权","z宅","O时空","仪手术","惠白","戴有","寇优","傅l","年安","曹太",
            "随厨房","冯多","聂企","卫向","扈女儿","皮班级","乔朱","邬卡","吉以前","戎课文","阙刑","竹师","沈必","蹇毕","皮文化","乌行人",
            "韦江","堵空间","繁风","李丘","秘纪","傅画笔","段拖鞋","红未","佘道路","法G","段年轮","麦广播","骆总结","璩方","帅起点","季忙",
            "朱乙","邓乃","阙乓","邢时空","蔚之","索历","向书房","鞠H","阚找","胡抹布","芮田","阚机会","召伪","g友","慕火苗","广众","齐杂",
            "毕烟雾","莫功","归尺","乐士","K问","伊口","诸抹布","时地","酆L","浦夸","练红","冀代","岳烟雾","匡吗","吕手工","枚勾","薛乔",
            "涂饥","支拖鞋","C瓦","伍旁边","薛先","闵宅","弥办法","冉种子","农轧","凤乡","x心里","吉坏","O礼物","司导","辛加","唐收","乐切",
            "孟小说","何语言","闻匠","莘创意","松许","阎讨","支乐园","况过去","隆姨妈","苌反","毕阳台","风乡下","卫井","施照片","岳学校",
            "舜本领","孔月","滑父","林叫","汲下","凤抚","娄晚会","祭士","汤兄","杞F","龙麦","赖设","平干","檀对比","栾对手","冷黄色","i朵",
            "时手术","温己","风本子","茅伍","项身影","宁早晨","真尽","曲买","邬毛衣","臧年级","f家长","怀画家","庄州","韩用心","孟孔","端X",
            "周后果","s毛","魏们","奶油苌w","山动","伊寸","扈园丁","白斗","e沙子","全胖子","封中午","宓认识"
    };

    public static String getName(){
        StringBuffer sb = new StringBuffer();
        sb.append(firstNames[MD5.random.nextInt(firstNames.length)]);
        sb.append(lastNames[MD5.random.nextInt(lastNames.length)]);
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println(getNameList());
    }

    public static List<String> getNameList(){
        int length = (MD5.random.nextInt(150)+250);
        List<String> nameList = new LinkedList<>();
        for(; length>=0; length -= 1){
            Boolean flag = true;
            while (flag){
                int i = MD5.random.nextInt(1000);
                if(!nameList.contains(names[i])){
                    if(lastNames.length < i){
                        nameList.add(names[i]);
                    }else{
                        nameList.add(names[i]+lastNames[i]);
                    }
                    flag = false;
                }
            }
        }
        return nameList;
    }

}
