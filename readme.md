```markdown
# 题目：新闻客户端APP

## 1. 概述

### 1.1 题目

新闻客户端 APP 主要功能包括：基于 Android 网络编程，通过网络爬虫，爬取某新闻网站新闻（新闻数据源自拟），通过解析、分类、列表显示，单击新闻可查看新闻相关内容。可模拟借鉴今日头条 APP。

界面参考:
![界面参考图]()

### 1.2 环境介绍
![环境介绍图]()

## 2. 系统设计

### 2.1 关键技术

- （1）调用API获取网络数据
- （2）RecyclerView实现新闻列表
- （3）使用SQLlite实现保存浏览历史

### 2.2 系统设计

#### 2.2.1 系统功能结构图

![系统功能结构图](图2.1 系统功能结构图)

#### 2.2.2 数据库设计

| 序号 | 字段名 | 字段类型 | 是否主键 | 是否为空 | 备注信息 |
|------|--------|----------|----------|----------|----------|
| 01 | history_id | integer | 是 | 否 | 历史记录ID |
| 02 | Username | text | 否 | 否 | 用户名 |
| 03 | uniquekey | text | 否 | 否 | 新闻id |
| 04 | new_json | text | 否 | 否 | 新闻数据 |

#### 2.2.3 主题对象设计

**（1）HistoryInfo**

主要包含history_id（历史记录ID），uniquekey（新闻ID），username（用户名），new_json（新闻数据）。

**（2）NewsInfo**

主要包含reason，result，error_code，该主题对象是GsonFormatPlus插件根据API接口返回的数据自动生成。

**（3）TitleInfo**

主要包含title（标题）和py_title（拼音标题）。

## 3. 系统实现

### 3.1 主界面模块

#### 3.1.1 代码实现部分

它使用了多个UI组件，如TabLayout、ViewPager2、NavigationView和DrawerLayout，来构建一个具有滑动标签页和侧边导航栏的界面。

**（1）成员变量**

- `titles`: 一个List<TitleInfo>类型的列表，用于存储标签页的标题和对应的拼音缩写。
- `tab_layout`: TabLayout的实例，用于在顶部显示标签页。
- `viewPager`: ViewPager2的实例，用于实现标签页的滑动功能。
- `nav_view`: NavigationView的实例，与DrawerLayout一起使用，用于实现侧边导航栏。
- `btn_open`: 一个ImageView的实例，是一个按钮，用于打开侧边导航栏。
- `drawer_Layout`: DrawerLayout的实例，是侧边导航栏的容器。

**（2）onCreate()方法**

- 初始化数据：向titles列表中添加了一系列TitleInfo对象，每个对象代表一个标签页的标题和拼音缩写。
- 初始化控件：通过findViewById方法获取UI组件的实例，并设置侧边导航栏的打开按钮的点击监听器。
- 设置侧边导航栏的点击事件：为NavigationView设置点击事件监听器，根据点击的菜单项执行不同的操作，如跳转到新的Activity或模拟按下Home键。
- 设置ViewPager的Adapter：使用FragmentStateAdapter为ViewPager2设置适配器，根据titles列表中的数据动态创建TabNewsFragment实例。
- 设置TabLayout的选中监听器：当标签页被选中时，通过TabLayout.OnTabSelectedListener更新ViewPager2的当前项。
- 使用TabLayoutMediator连接TabLayout和ViewPager2：TabLayoutMediator用于自动同步TabLayout和ViewPager2的选中状态，并设置标签页的文本。

#### 3.1.2 界面设计部分

这是一个带有抽屉式导航的Android应用界面，用户可以通过滑动屏幕边缘来访问侧边菜单。

![主界面设计]()

### 3.2 API获取新闻数据模块

#### 3.2.1 代码实现部分

这段代码定义了一个名为TabNewsFragment的 Android Fragment，它主要用于从一个网络API获取新闻数据，并将这些数据展示在RecyclerView中。当用户点击某个新闻项时，会跳转到 NewsDetailsActivity 以显示新闻的详细信息。

**（1）成员变量**

- `url`：定义了从哪个URL获取新闻数据，URL中包含了一个查询参数key和一个占位符type=，type=会被实际的新闻类型替换。
- `recyclerView`：用于展示新闻列表的 RecyclerView 控件。
- `rootView`：Fragment的根视图。
- `ARG_PARAM`：一个常量，用于在Fragment的参数Bundle中存储新闻类型。
- `title`：从外部传入，表示新闻的类型，用于构建请求URL。
- `newsListAdapter`：RecyclerView的适配器，用于将新闻数据绑定到视图上。
- `mHandler`：一个Handler对象，用于在主线程中处理从网络线程发送的消息，以更新UI。

**（2）各个方法**

- `newInstance`：一个静态方法，用于创建TabNewsFragment的实例并设置其参数。
- `onCreate`：在Fragment被创建时调用，用于初始化参数。
- `onCreateView`：用于创建Fragment的视图并初始化了recyclerView。
- `onActivityCreated`：在Fragment的Activity被创建后调用，它初始化了适配器，设置了适配器的点击监听器，并启动了网络请求来获取新闻数据。
- `getHttpData()`：使用OkHttp库发起网络请求，从指定的URL获取新闻数据。

#### 3.2.2 界面设计部分

这是一个使用了LinearLayoutCompat作为根视图，并使用RecyclerView来显示列表项的布局文件。

![API获取界面设计]()

#### 3.2.3 适配器NewsListAdapter

这段代码定义了一个名为NewsListAdapter的类，它用于在RecyclerView中展示新闻列表的数据。是为了处理NewsInfo.ResultBean.DataBean类型的数据而设计，这些数据来自聚合新闻API的响应。

**（1）成员变量**

- `mDataBeanList`: 用于存储新闻数据NewsInfo，ResultBean，DataBean类型的列表。
- `mContext`: 用于在适配器中访问上下文，例如加载图片或执行其他需要上下文的操作。
- `monItemClickListener`: 一个接口实例，用于处理条目点击事件。

**（2）各个方法，类，接口**

- `setListData`: 用于设置适配器的数据源。在设置了新的数据源后，调用notifyDataSetChanged()方法来通知RecyclerView更新其内容。
- `onCreateViewHolder`: 用于创建RecyclerView中的新条目视图。通过LayoutInflater加载了一个名为news_list_item的布局文件，并创建了一个MyHolder实例来持有这个视图的引用。
- `onBindViewHolder`: 用于将数据源中的数据绑定到ViewHolder的视图上。同时，也为整个条目视图设置了点击监听器，以便在点击时执行自定义操作。
- `getItemCount()`: 返回适配器中的数据项数量，这里返回mDataBeanList的大小。
- `MyHolder`: 一个静态内部类，继承自RecyclerView.ViewHolder。它包含了RecyclerView条目中所有需要访问的视图组件的引用，并在构造函数中通过findViewById方法初始化这些引用。
- `onItemClickListener`: 一个接口，定义了onItemClick方法，用于处理条目的点击事件。

### 3.3 新闻详情模块

#### 3.3.1 代码实现部分

NewsDetailsActivity类设置了一个基本的框架来显示新闻详情，包括从上一个活动接收数据、设置标题和加载网页，以及处理返回操作和添加历史记录。

- `setContentView()`: 加载布局文件activity_news_details.xml，该文件应该包含了一个Toolbar控件和一个WebView控件。
- `getIntent().getSerializableExtra("dataBean");`从启动这个活动的Intent中获取NewsInfo.ResultBean.DataBean类型的序列化数据。
- `toolbar.setOnClickListener()`:为toolbar设置了一个点击监听器，当点击toolbar时，调finish()方法结束当前活动，返回上一个活动。
- 添加历史记录部分: 使用Gson库将dataBean对象转换为 JSON 字符串，然后通过HistoryDbHelper的功能将这个记录添加到历史记录数据库中。

#### 3.3.2 界面设计部分

该XML布局文件定义了一个垂直排列的线性布局（LinearLayoutCompat），包含两个主要元素：工具栏（Toolbar）：设置了标题、标题文本颜色、背景以及导航图标。WebView：用于显示网页内容，设置为填充整个屏幕。

![新闻详情界面设计]()

### 3.4 历史记录模块

#### 3.4.1 代码实现部分

HistoryListActivity的主要功能是显示历史新闻列表。

- Toolbar和RecyclerView：初始化了工具栏和RecyclerView，RecyclerView用于显示新闻列表。
- 数据加载：通过HistoryDbHelper查询历史数据，将JSON格式的数据解析成DataBean对象并添加到列表中。
- 适配器设置：设置NewsListAdapter并传递数据。
- 点击事件：为列表项添加点击监听器，点击时启动NewsDetailsActivity并传递所点击的新闻数据。
- 工具栏点击事件：点击工具栏时，结束当前活动。

#### 3.4.2 界面设计部分

HistoryListActivity的布局包含以下主要元素：工具栏（Toolbar）：设置标题为"浏览历史"，背景为紫色，左侧有返回图标。RecyclerView：用于显示历史新闻列表，采用线性布局管理器。

![历史记录界面设计]()

### 3.5 关于模块

#### 3.5.1 代码实现部分

AboutActivity的主要功能是展示关于页面。

- Toolbar初始化：在onCreate方法中，通过findViewById找到工具栏。
- 点击事件：设置工具栏的点击监听器，点击时调用finish()方法结束当前活动，返回到上一个页面。

#### 3.5.2 界面设计部分

该布局主要包含三个TextView显示开发者名称、应用版本和版本时间，并使用约束布局确保它们在页面中垂直排列，且居中对齐。

![关于界面设计]()

## 4. 系统测试

### 4.1 主界面模块

![主界面测试]()

### 4.2 API获取新闻数据模块

图片正常获取：![正常图片]()  
部分图片获取异常：![异常图片]()

### 4.3 新闻详情模块

![新闻详情测试]()

### 4.4 历史记录模块

![历史记录测试]()

### 4.5 关于模块

![关于模块测试]()

## 5. 设计总结

在这次移动软件平台开发的课程设计中，我投入了大量的时间和精力，最终成功开发出了一个功能较为完善的新闻客户端。通过这次实践，我深刻体会到了从理论到实践的跨越所带来的挑战与收获，感受到了编程的魅力和无限可能。

在项目的初期，我面临着如何高效、准确地从网络获取新闻数据的问题。通过查阅大量资料和文档，我逐渐掌握了使用HTTP请求库（如OkHttp）来调用API，并解析返回的JSON数据。这个过程不仅锻炼了我的自学能力，还让我对网络通信有了更深入的理解。当我看到新闻数据成功显示在屏幕上时，那种成就感是无法用言语表达的。

在项目的开发中，RecyclerView是一个功能强大的列表控件，它不仅可以显示简单的列表项，还可以实现复杂的布局和动画效果。在开发新闻列表时，我遇到了很多挑战，如如何优化列表的滚动性能、如何设置点击事件等。通过不断地尝试和调试，我逐渐掌握了RecyclerView的使用技巧，并成功地将新闻数据以列表的形式展示给用户。这个过程让我深刻体会到了"实践出真知"的道理。

此外为了实现保存用户浏览历史的功能，我选择了SQLite数据库。虽然我之前对数据库有一定的了解，但真正动手实现时还是遇到了很多困难。从设计数据库表结构、编写SQL语句到实现数据的增删改查，每一步都需要我细心思考和不断尝试。最终，我成功地实现了保存和查询浏览历史的功能，这让我对数据库的应用有了更深入的认识。

总之，这次课程设计对我来说是一次宝贵的经历。它不仅让我学到了很多知识和技能，更让我深刻体会到了编程的魅力和无限可能。我相信，在未来的学习和工作中，我会继续保持这种积极向上的态度和精神，不断追求进步和突破。
```