# 代码解读 Code interpretation

## 记事本功能 NoteBook funuction

包含两个部分，一个是部分单纯的文本记录功能，还有一个是每一个笔记的提醒时间设置功能。每一个笔记的id、标题、内容和提醒时间等相关信息都作为Note类的属性进行存放。

![1592176561(1)](media/47c5aed36e2b261075828252a9f541ea.png)

SQLite数据库中表的创建也是按照Note类的属性的。

![1592176932(1)](media/e210605ff2898ccd08e507116c928a43.png)

用户点击首页右上角的“+”号创建笔记，笔记的创建需要用户输入标题和内容，提醒时间如果没有设置就会使用默认的提醒时间，提交笔记保存后会判断笔记输入的内容是否为空，即文本长度为0，然后将输入的内容封装成Note类型的对象，然后调用SimpleDatabase类中的addNote方法，传递参数为Note对象，使用ContentValues
插入数据，每一个笔记的id属性都是由插入数据库中存储后才产生并返回的，id是唯一的所以可以根据笔记的id进行查找。

![1592176879(1)](media/a4250574ae8c07be4f58f1d8535ed4f3.png)![](media/e2334b8f59021061425c7e0e56dd6e02.png)

首页展示用户的已有的笔记列表时会使用List\<Note\>存储从数据库查找的所有已经创建保存的笔记。如果用户还没有保存过笔记，即List\<Note\>是空的，就展示activity_main.xml中TextView“未记过笔记，请点右上方+进行创建”的提示信息。

![1592177673(1)](media/d467c79d939c6e993b61815526992341.png)

![1592177819(1)](media/531a58d21f47e55c66d530aaf9045aaa.png)

如果搜寻结果不为空，即用户记录过笔记，就用displayList方法将List\<Note\>传递进去，借助RecyclerView、Adapter显示数据。

![1592177959(1)](media/27ca9797c395f1f620f18f1a054a69d9.png)

![1592178238](media/95a5d7c8f7c3fd62be93056134a0387a.png)

首页中笔记的显示还要添加被选时允许展示笔记详情内容的事件，用的是itemView的setOnClickListener，点击后会跳转到对应笔记的内容显示，用户的是笔记的ID属性进行Activity的跳转。

![1592178736(1)](media/b510802023d1529c91cf26e43e415149.png)

其中一个重要的方法是通过id获取笔记，利用id作为数据库查找的依据。

![](media/1bf4e6b05a238eb55b15fa11e035d7f1.png)

此时界面右下方的FloatingActionButton提供了删除功能，设置的点击事件为笔记删除，用的是SimpleDatabase的deleNote方法，传入了Note对象的id属性进行删除，并且用Toast提醒用户笔记已经被删除的信息。

![1592178849(1)](media/078105eed1abfaa69774d35441750231.png)

![1592178803(1)](media/57e02167c58e63e2a5927fa94e54f5ee.png)

笔记详情展示界面还有允许用户编辑修改的功能，编辑按钮在Toolbar上面的Menu中，如果选择的是edit的item就要跳转到编辑界面，这个界面与笔记创建界面几乎相同，但是完成编辑后调用的是SimpleDatabase的editNote方法，同样是传入将笔记的id、标题、时间、内容等信息封装而成的对象，在数据库中的更新也是借助笔记的id进行的。

![1592179042(1)](media/e2bb2f26a5eebb17d0bcfcfb020c2c22.png)

![1592179204(1)](media/794b2dbbf2a9ec5b5135c464a707d0e1.png)

## 闹钟提醒功能 Clock function

ClockManager类中存放的是获取系统闹钟服务、取消闹钟以及添加脑中的服务，在添加闹钟与更新闹钟均使用addAlarm函数，在函数中，添加闹钟前，如果已经添加过闹钟，则该闹钟被取消。

![](media/5183aa28cea9fad0ac368082da3ea3cb.png)

在名字为edit和add_note的xml文件中，添加了提醒时间框。

![](media/fa946ecf8e35b59e8964f9e73781d6d9.png)

在EditText中设置参数，让其不可输入但是可以点击

![](media/9583122a81511bdf2423f165a7d30e04.png)

在add_note类中datePickClick在点击该EditText的时候被调用。弹出时间选择器供用户选择时间

![](media/14d97e1e74e0a7df111566ad2ebb97cd.png)

![cce31264977b20b35cf20d5c619722c](media/278615db5b1982ddcf045d5713263817.jpeg)

点击保存后，以下代码会在系统中添加闹钟用于提醒

![](media/7245fa8ff1a0c68ef551851a859d65ca.png)

到时间后，ClockReceive会接收到广播，onReceive函数接收到广播后调用postToClockActivity函数来启动ClockActivity类，ClockActivity调用函数clock（）；

![](media/d4bec1a5de66515391c9f0681f7e4c27.png)

![](media/6694a6b40ec845fb7b676f19e6358d97.png)

弹出dialog_alarm_layout布局，用于提醒用户事件未完成

![0194ac593952ca6dc17f4ce3b24ec54](media/bd08f712c854383e1f8cf9d19f079d90.jpeg)

函数中以下三个函数让提醒的界面有闹铃声音以及震动

![](media/149fd1f724e076cc733b86b990cc0ac9.png)

如果屏幕没有唤醒则调用wakeUpAndUnlock函数

![](media/1161755308813e2fb6b9cb81b094ebce.png)

## 天气功能 Weather forcast function

XML文件：activity_weather和weather_item。

activity_weather是天气界面的布局，上方为一个actionBar，actionbar中有返回主页按钮、“天气”字样，下方为一个textView和一个ListView，textView显示当前城市，ListView用于存放天气相关信息。

![](media/f2960519140313f45d7b155cc146c258.png)

![](media/d008a84afe4c32552fc83d9ea5c56708.png)

Weather_item中有五个TextView，分别用来存放date,text_day,text_night,high,low。（日期，晨间天气，夜间天气，最高气温，最低气温）

![](media/ccf5dcaa3d4f20a4f72f966720d2091a.png)

![](media/44c926ab70dfb35d242fa391c385cf28.png)

Java文件：WeatherInfo Weather JsonParser

WeatherInfo中写了date,text_day,text_night,high,low六个属性的setter和getter。

![](media/0857fc89ab67b8109eaea55e219f5370.png)

![](media/da681aa19180c84f939f44814a8506be.png)

JsonParser用于解析心知天气api的信息

![](media/380a355a83f097fdb8b7efc003f33c02.png)

![](media/fb20151ae0338bb6478b35c97d02d59a.png)

## 使用到的API接口：

在WeatherInfo中，有city,date,text_day,text_night,high,low六个属性，以及setter
getter函数，这六个属性是要从seniverse心知天气这个api中获得的属性。

![](media/86363436d31cb6a14df23ac981fad773.png)

![](media/2292cab726dba6acec618093a5f7da17.png)

在JsonParser中，创建一个arrayList，用于填入天气的相关信息。创建WeatherInfo类变量weatherInfo，并设置weatherInfo的city属性，根据不同的city调用api,并通过JsonArray、JSONObject来解析api中的信息，并获得该城市的date,
text_day,text_night,high,low。

![](media/3d22f0c8a5b3d8c331412259eccbaf2f.png)

获得JsonArray后，通过遍历，取出的每一个元素都是JsonObject对象，每个JsonObject对象里都会包含date,text_daty,text_night,high,low数据，调用getString即可取出这些数据。![](media/7e241328adaaa6d31f98717b00868192.png)

在weather中，设定的默认city为beijing，并将city填于url中，利用心知天气的链接获取北京的天气数据，并将数据以String的形式填入arrayList。![](media/3a4790658707870130c8ee16d27ede8d.png)

这里我们使用了HttpURLConnection发起HTTP请求，并解析服务器返回的数据![](media/3a4790658707870130c8ee16d27ede8d.png)

Weather的内部类Adapter继承了BaseAdapter，在getView函数中，使用inflate找到weather_item，并通过findViewById函数，获取date,text_day,text_night,high,low的TextView，再将arrayList中的数据利用setText传入TextView中显示。

![](media/9cf8585f41f63dcd8ad9994cecbc3cb1.png)

![](media/9518f2ed84a5882299e0d4a35ea3ca83.png)

![](media/18fe0ae0dfce2e26d3d4f0e344a9ddfc.png)
