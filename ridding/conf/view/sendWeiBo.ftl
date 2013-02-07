<!DOCTYPE HTML>
<#escape x as x?html>
<#assign pageName = "sendWeiBo" />
<#include "head.ftl">
<style>
html,body,#gmap{height:100%; margin:0;}
body{font-size:83%;}
#help{padding-top:20%; text-align:center;}
</style>

<body>

<h3 style="font-size: 20px;color: red;"><a href="/backend/index/">返回</a></h3>

<h3 style="font-size: 20px;color: red;">发微博功能</h3>
<div>
<span>总数为:</span>
</div>

<div style="height:300px;">
    <h3>新建一条记录</h3> 
    <div>
    <span>微博内容:</span><br>
    <textarea rows="2" cols="60" name="WeiBoContext"></textarea>
    </div>
    <span>微博图片:</span>
    <form onsubmit="return saveReport();" class="t" size="37" enctype="multipart/form-data" method="post" target="uploadFrame" action="/user/54/photoUpload/" id="uploadFrame">
         <input type="file" class="t file l " size="37" name="file" value="浏览">
         <input type="submit" value="上传图片">
    </form>
    <div>
     <span>发送时间 :</span><input type="text" id="weiboSendTime" maxlength="16" onfocus="jQuery(this).calendar()" readonly="true">
    </div>
    <div style="position: absolute; left: 106px; top: 52px; display: none;" id="calendar_div"></div>
    <span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;url&nbsp;　:</span><input type="text" id="urlText"><input type="button" id="urlBtn" value="确定">
    <div>
        <img id="image" src="" style="width: 100px; height: 100px;display:none;">
    </div>
    <div>
       <span>资源类型:</span>
       <select id="weiboSelector">
          <option selected="" value="1">&nbsp;&nbsp;&nbsp;&nbsp;新浪微博&nbsp;&nbsp;&nbsp;&nbsp;</option>
          <option value="2">&nbsp;&nbsp;&nbsp;&nbsp;腾讯微博&nbsp;&nbsp;&nbsp;&nbsp;</option>
        </select>
    </div>
    <div>
       <span>微博类型:</span>
       <select id="weiboTypeSelector">
          <option selected="" value="0">&nbsp;&nbsp;&nbsp;&nbsp;普通微博</option>
          <option value="1">骑行地图微博</option>
        </select>
        <div>
           <span>&nbsp;&nbsp;&nbsp;活动Id&nbsp;: </span><input type="text" id="mapId">
        </div>
    </div>
    <div>
       <input type="button" value="提交微博" id="submitWeiBo">
    </div>
</div>


<div style="display:none;">
    <p style="font-size: 20px;color: red;">发送iphone推送信息</p>
    <input type="text" id="apnsValue" style="width:200px;"/>
    <input type="button" id="sendApns" value="发送"/>
</div>




<table border="1">
<thead>
<tr><td>id</td><td>内容</td><td>图片</td><td>发送源</td><td>发送时间</td><td>状态</td><td>操作</td></tr>
</thead>
<tbody>
<#if weiboList?exists>
<#list weiboList as weibo>
<tr id="${weibo.id!0}">
<td>${weibo.id!0}</td>
<td>${weibo.text!0}</td>
<td><a href="" target="_blank"><img style="max-width: 200px; max-height: 200px;" src="${cfg_imageHost}${weibo.photoUrl!""}"/></a></td>
<td><#if weibo.sourceType==1>新浪微博</#if></td>
<td width="300">${weibo.sendTimeStr!""}</td>
<td ><span style="color:red"><#if weibo.status==0>未发布<#else>已发布</#if></span></td>
</tr>
</#list>
</#if>
</tbody>
</table>
<#include "footer.ftl">
</body>
<#include "js.ftl">
</html>
</#escape>
