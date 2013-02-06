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
    <span>权重</span><input type="text" id="recom_weight" style="width:50px;"/>
    <span>图片url</span><input type="text" id="recom_firstpicurl" style="width:500px;"/>
    <span>链接文本(10个字)</span><input type="text" id="recom_ad_text" style="width:500px;"/>
    <span>链接图片url</span><input type="text" id="recom_ad_image_url" style="width:500px;"/>
    <span>链接url</span><input type="text" id="recom_link_url" style="width:500px;"/>
    <input type="button" id="recom" value="确定"/>
</div>


<#if riddingList?exists>
<div>
<#list riddingList as ridding>
<table border="1">
    <thead>
         <tr><td>骑行id</td><td>名称</td><td>路线封面</td><td>图片列表</td><td>权重</td><td style="width:100px">更新</td></tr>
    </thead>
    <tbody>
         <tr>
            <td><span>${ridding.id!0}</span></td>
            <td><span>${ridding.name!""}</span></td>
            <td><img style="width:100px;height:100px;" src="${ridding.aPublic.firstPicUrl!""}" id="recom_coverImg"/></td>
            <td>
              <#if ridding.riddingPictureList?exists>
                <#list ridding.riddingPictureList as picture>
                <div style="float:left;width:120px;height:140px;">
                   <img style="max-width:100px;max-height:100px;" src="${picture.photoUrl!""}" id="recom_img_${picture.id!0}"/>
                   <p><span>${picture.description}</span></p>
                   <p><a href="javascript:void(0);;" class="recom_img_recom" data-id="${ridding.aPublic.id!0}" data-url="${picture.photoUrl!""}">设为封面</a></p>
                </div>
                </#list>
              </#if> 
            </td>
            <td>
               <input type="text" id="recom_weight" value="${ridding.aPublic.weight!0}"/>
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