# zintow-text

* 用于中文文本清洗(侧重于文本标准化、短语抽取)的Java项目
* 技术方面采用正则表达式实现文本标准化、采用语法树实现短语抽取
* 处理流程方面首先根据condition推测文本所属的领域,然后创建出可能性最大(score值最大)的领域对应的文本标准化实例和短语抽取实例,实现文本清洗功能
* 采用的中文分词算法是[ansj_seg](https://github.com/NLPchina/ansj_seg)

## 快速使用
```
//创建领域条件
Map<String, String> conditions = new HashMap<>();
conditions.put("tags", "测试");
conditions.put("others", "系统");//其他取值参考resources/domain/domain_rules.yml
//文本标准化
String original = "价格真心给力";
String standardText = StandardizationFactory.createInstance(conditions).standardize(original);
System.out.println(standardText);//价格低。
//短语抽取
Extractor extractor = ExtractorFactory.createInstance(conditions);
String phrase = extractor.getPhrase(standardText);
System.out.println(phrase);//价格低:ph。价格低即为抽取出的短语,ph是短语的标识,可以认为是中文分词中的自定义词性。
//其他功能(文本分词)
String tokens = extractor.getToken("价格低");
System.out.println(tokens);//价格:n 低:a
```
## 自定义修改

可以修改resources目录下的文件满足自定义需求。

### 1.　resources/dictionary/*.dic

文本分词的词典,可以根据需要进行添加扩展。格式为"颜值	n	1000",以\t为分隔符。

第一列是中文词语,第二列是词性(可自定义字符),第三列是词频,理论上词频要从语料库中统计,也可简单的赋一个值。

### 2.　resources/domain/*.yml

领域判断规则。

```
test:
  tags:
    - 测试: 1.0
    - 测验: 1.0
  others:
    - 系统: 0.5　
```
test即为领域名称,tags、others是领域判断依据,测试、测验等是属于tags的具体判断规则,后面的数字是score。领域、依据、规则的名称和个数不限。

多个领域的情形下会根据score进行排序,score越大表明属于这个领域的可能性越大。

注意:添加一个新的领域名称XXX及其规则时,需要添加XXX.dic,XXX_phrase_grammar.yml,XXX.sta三个文件,否则会出现异常。

### 3.　resources/phrase/*.yml

短语抽取语法树规则。
```
settings:
　filtered:
　　-　n
　　-　不
　　-　a
rules:
　/n$:
　　successors:
　　　/a$:
　　　　score: 0.5
```
filtered表示短语中只包含"名词(n)、不(否定词)、形容词(a)"。

短语规则如上,名词(n)+形容词(a)构成一个短语,这个短语的score是0.5。注意"/n$"是正则表达式。

规定score只出现在语法树叶子节点,并且深度相同的节点的score也是相同的,深度越深score越大。

### 4.　resources/standardization/*.sta

文本标准化正则表达式。格式为"价格低	价格{0,5}给力",以\t为分隔符,第一列是标准说法,第二列是非标准说法的正则表达式,或者同义词、错别字等。
