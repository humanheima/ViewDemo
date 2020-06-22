![在这里插入图片描述](https://img-blog.csdnimg.cn/20200406215604618.jpeg?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2xlaWxpZmVuZ3hpbmdtdw==,size_16,color_FFFFFF,t_70#pic_center)

注意：在使用PorterDuffXfermode的时候，目标图像（DST）和图像（SRC）混合的操作要在一个新的图层上进行，否则当前的Canvas上的像素会影响混合操作。

```java
@Override
protected void onDraw(Canvas canvas) {
    //创建一个新的图层
    int layerId = canvas.saveLayer(new RectF(), null, Canvas.ALL_SAVE_FLAG);
    //...
    //目标图像（DST）和图像（SRC）混合的操作
    //...
    //将新的图层绘制到上一个图层或者屏幕上（如果没有上一个图层）。
    canvas.restoreToCount(layerId);
}
```

创建目标图像
```java
public Bitmap makeDst(int w, int h) {
    Bitmap bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
    Canvas c = new Canvas(bm);
    Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
    //设置的透明度是FF
    p.setColor(0xFFFFCC44);
    c.drawOval(new RectF(0, 0, w * 3 / 4f, h * 3 / 4f), p);
    return bm;
}
```
注意：我们画笔设置的透明度是完全不透明，在Android中完全不透明用float类型的数表示为1。![在这里插入图片描述](https://img-blog.csdnimg.cn/20200406223620358.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2xlaWxpZmVuZ3hpbmdtdw==,size_16,color_FFFFFF,t_70#pic_center)
注意：我们创建的目标图像的大小是红色框的大小，我专门给标了出来。只不过我们只在圆形区域绘制了黄色内容，黄色圆形区域的alpha是1，color是FFCC44。剩余其他部分没有绘制，也就是说其他部分的alpha是0，color也是0，是透明的。这一点很重要，因为在这个例子中，目标图像和源图像大小是一样的，图像的每个区域的alpha和color都会参与混合运算。

创建源图像
```java
Bitmap makeSrc(int w, int h) {
    Bitmap bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
    Canvas c = new Canvas(bm);
    Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
    //设置的透明度是FF
    p.setColor(0xFF66AAFF);
    c.drawRect(w / 3f, h / 3f, w * 19 / 20f, h * 19 / 20f, p);
    return bm;
}
```
注意：我们画笔设置的透明度是完全不透明，在Android中完全不透明用float类型的数表示为1。
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200406223911467.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2xlaWxpZmVuZ3hpbmdtdw==,size_16,color_FFFFFF,t_70#pic_center)
注意：我们创建的目标图像的大小是红色框的大小，我专门给标了出来。只不过我们只在矩形区域绘制了蓝色内容，蓝色矩形区域的alpha是1，color是66AAFF。剩余其他部分没有绘制，也就是说其他部分的alpha是0，color也是0，是透明的。这一点很重要，因为在这个例子中，目标图像和源图像大小是一样的，图像的每个区域的alpha和color都会参与混合运算。

PorterDuffXfermode的构造函数
```java
public PorterDuffXfermode(PorterDuff.Mode mode) {
    porterDuffMode = mode.nativeInt;
}
```
PorterDuff.Mode类，表示混合模式，枚举值有18个。
```java
    public enum Mode {
      
        CLEAR       (0),
        SRC         (1),
        DST         (2),
        SRC_OVER    (3),
        DST_OVER    (4),
        SRC_IN      (5),
        DST_IN      (6),
        SRC_OUT     (7),
        DST_OUT     (8),
        SRC_ATOP    (9),
        DST_ATOP    (10),
        XOR         (11),
        DARKEN      (16),
        LIGHTEN     (17),
        MULTIPLY    (13),
        SCREEN      (14),
        ADD         (12),
        OVERLAY     (15);

        Mode(int nativeInt) {
            this.nativeInt = nativeInt;
        }
        
        public final int nativeInt;
    }
```

定义：
>Sa：全称为Source alpha，表示源图的Alpha通道
Sc：全称为Source color，表示源图的颜色
Da：全称为Destination alpha，表示目标图的Alpha通道
Dc：全称为Destination color，表示目标图的颜色

混合结果分为两部分[Ra , Rc]，Ra表示结果的alpha部分，Rc表示计算结果的color部分。

各种混合模式的计算

#### CLEAR
 计算结果：[0, 0]，目标图像被源图像覆盖的区域清空。在这个例子中，源图像大小和目标图像是一样大的，就是红色框所示大小。最终显示结果是透明的。
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200407080029422.png#pic_center)
#### SRC

计算结果：[Sa, Sc]，源图像的像素替代目标图像的像素。注意是**替代**。
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200407080424735.png#pic_center)

### DST
计算结果：[Da, Dc]，源图像像素被丢弃，保留目标图像像素不变。
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200407080525100.png#pic_center)

#### SRC_OVER
计算结果：[Sa+(1-Sa)*Da,  Sc+(1-Sa)*Dc]，源像素会覆盖在目标像素之上。在这个例子中：
1. 源图像的蓝色矩形区域透明度是1，计算结果是[Sa, Sc]。
2. 源图像的其他区域的透明度是0，其他区域和目标图像的黄色圆形部分相交的区域计算结果是[Da, Dc]。
3. 源图像的其他区域的透明度是0，颜色也为0。目标图像的其他区域的透明度是0，颜色也为0，计算结果是[0, 0]。

所以最终的显示效果如下所示：
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200406231506419.png#pic_center)

#### DST_OVER
计算结果：[Da+(1-Da)*Sa,  Dc+(1-Da)*Sc]，源像素在目标像素之后绘制。在这个例子中：
1. 目标图像的黄色圆形区域透明度是1，计算结果是[Da, Dc]。
2. 目标图像的其他区域的alpha=0，color=0，其他区域和源图像的蓝色矩形部分相交的区域计算结果是[Sa, Sc]。
3. 目标图像的其他区域的alpha=0，color=0，，源图像的其他区域的alpha=0，color=0，，计算结果是[0, 0]。

#### SRC_IN
计算结果：[Sa\*Da,  Sc\*Da]，保留源图像覆盖目标图像的区域的源图像的像素，其他区域的像素全部丢弃。在这个例子中：
1. 源图像完全覆盖目标图像，源图像的蓝色矩形部分和目标图像的黄色圆形区域相交的区域是一个扇形，计算结果是[Sa, Sc]。
2. 源图像的蓝色矩形部分和目标图像的黄色圆形区域不相交的区域，计算结果是[ 0, 0 ]。
4. 其他区域的像素全部丢弃，计算结果是[0, 0]。

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200406233951454.png#pic_center)

#### DST_IN
计算结果：[Sa\*Da,  Dc\*Sa]，在目标图像和源图像相交的区域，保留目标图像的像素，其他区域的像素全部丢弃。在这个例子中：
1. 目标图像和源图像完全相交，目标图像的黄色区域和源图像蓝色区域相交的区域，计算结果是[Da, Dc]。
2. 目标图像的黄色区域和源图像蓝色区域不相交的区域，计算结果是[0, 0]。
3. 其他区域的像素全部丢弃，计算结果是[0, 0]。

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200407074058966.png#pic_center)
#### SRC_OUT
计算结果：[ ( 1 -  Da ) \* Sa ,  ( 1 -  Da ) \* Sc]，保留源图像有像素的区域没有覆盖目标图像有像素的区域的像素。丢弃源图像有像素的区域覆盖目标图像有像素的区域的像素。丢弃所有的目标图像像素。在这个例子中：
1. 保留源图像有像素的区域没有覆盖目标图像有像素的区域的像素。计算结果是[ Sa , Sc ]。
2. 丢弃源图像有像素的区域覆盖目标图像有像素的区域的像素。计算结果是[ 0 , 0 ]。
3. 丢弃所有的目标图像像素。计算结果是[ 0 , 0 ]。



#### DST_OUT
#### SRC_ATOP
#### DST_ATOP
        XOR        
        DARKEN     
        LIGHTEN    
        MULTIPLY   
        SCREEN    
        ADD        
        OVERLAY  





