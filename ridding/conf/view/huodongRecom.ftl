<!DOCTYPE HTML>
<#escape x as x?html>
<#assign pageName = "huodongRecom" />
<#include "head.ftl">
<style>
html,body,#gmap{height:100%; margin:0;}
body{font-size:83%;}
#help{padding-top:20%; text-align:center;}
</style>
<body>
<h3 style="font-size: 20px;color: red;"><a href="/backend/index/">返回</a></h3>
<div style="">
    <p style="font-size: 20px;color: red;">添加推荐信息</p>
    <span>骑行Id</span><input type="text" id="recom_riddingId" style="width:50px;"/>
    <span>用户Id</span><input type="text" id="recom_userId" style="width:50px;"/>
    <span>权重</span><input type="text" id="recom_weight" style="width:50px;"/>
    <p>上传封面(可以不设置封面，取第一张)</p>
    <form id="recom_uploadFrame" action="/user/${visitUserId}/photoUpload/" target="uploadFrame" method="post" enctype="multipart/form-data" size="37" class="t" onsubmit="return saveRecomReport();">
         <input type="file" value="浏览" name="file" size="37" class="t file l "/>
         <input type="submit" value="上传图片"/>
    </form>
    <div>
        <img style="width: 100px; height: 100px;display:none;" src=""  id="recom_coverimage"/>
    </div>
    <input type="button" id="recom" value="确定"/>
</div>


<#if riddingList?exists>
<div>
<#list riddingList as ridding>
<table border="1">
    <thead>
         <tr><td>id</td><td>名称</td><td>路线封面</td><td>图片列表</td><td>权重</td><td style="width:100px">更新</td></tr>
    </thead>
    <tbody>
         <tr>
            <td><span>${ridding.id!0}</span></td>
            <td><input type="text" id="recom_name_${ridding.id!0}" value="${ridding.name!""}" /></td>
            <td><img style="width:100px;height:100px;" src="${cfg_imageHost}${ridding.firstPicUrl!""}" id="recom_coverImg"/></td>
            <td>
              <#if ridding.riddingPictureList?exists>
                <#list ridding.riddingPictureList as picture>
                <div style="float:left;width:120px;height:140px;">
                   <img style="max-width:100px;max-height:100px;" src="${cfg_imageHost}${picture.photoUrl!""}" id="recom_img_${picture.id!0}"/>
                   <p><span>${picture.description}</span></p>
                   <p><a href="javascript:void(0);;" id="recom_img_recom_${picture.id!0}">设为封面</a></p>
                </div>
                </#list>
              </#if> 
            </td>
            <td>
               <input type="text" id="recom_weight" value="${ridding.weight!0}"/>
            </td>
            <td><a href="javascript:void(0);;" onClick="updateRecom(${ridding.leaderUserId}),${ridding.id});">更新</a><a href="javascript:void(0);;" id="recom_update">删除</a></td>
         </tr>
    </tbody>
</table>
</#list>
</div>
</#if>





<#include "footer.ftl">
</body>
<script>

</script>
</html>
</#escape>
<#include "js.ftl">