<!DOCTYPE HTML>
<#escape x as x?html>
<#assign pageName = "huodongList" />
<#include "head.ftl">
<style>
html,body,#gmap{height:100%; margin:0;}
body{font-size:83%;}
#help{padding-top:20%; text-align:center;}
</style>
<body>
<h3 style="font-size: 20px;color: red;"><a href="/backend/index/">返回</a></h3>
<div style="">
    <input type="text" value="输入用户名"/>
    <input type="text" value="输入用户id"/>
    <input type="button" id="search" value="搜索"/>
</div>


<#if riddingList?exists>
<div>
<#list riddingList as ridding>
<table border="1">
    <thead>
         <tr><td>id</td><td>名称</td><td>路线封面</td><td>图片列表</td><td>权重</td><td>更新</td></tr>
    </thead>
    <tbody>
         <tr>
            <td><span>${ridding.id!0}</span></td>
            <td><input type="text" id="recom_name_${ridding.id!0}" value="${ridding.name!""}" /></td>
            <td><img style="width:100px;height:100px;" src="${cfg_imageHost}${ridding.firstPicUrl!""}" id="recom_coverImg"/></td>
            <td>
              <#if ridding.riddingPictureList?exists>
                <#list ridding.riddingPictureList as picture>
                   <img style="width:100px;height:100px;" src="${cfg_imageHost}${picture.photoUrl}" id="recom_img_${picture.id}"/>
                   <span>${photoUrl.description}</span>
                   <a href="javascript:void(0);;" id="recom_img_delete_${picture.id}">删除</a>
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