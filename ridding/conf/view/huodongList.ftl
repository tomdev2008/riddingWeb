<!DOCTYPE HTML>
<#escape x as x?html>
<#assign pageName = "huodongList" />
<#include "head.ftl">
<style type="text/css">
html,body,#gmap{height:100%; margin:0;}
body{font-size:83%;}
#help{padding-top:20%; text-align:center;}
.picture {
  float: left;
  clear behind;
  }
</style>

<body>
<h3 style="font-size: 20px;color: red;"><a href="/backend/index/">返回</a></h3>

<div style="">
    <input type="text" id="search_username" value="输入用户名"/>
    <input type="text" id="search_userid" value="输入用户id"/>
    <input type="button" id="search" value="搜索"/>
</div>
<div>
    <h3><a href="/Ridding/backend.do?action=huodongList&requestTime=${bottomUpdateTime!"-1"}&nextOrBefore=0">默认排序</a></h3>
    <h3><a href="/Ridding/backend.do?action=huodongList&orderByLike=1">喜欢数排序</a></h3>
    <h3><a href="/Ridding/backend.do?action=huodongList&orderByComment=1">评论数排序</a></h3>
    <h3><a href="/Ridding/backend.do?action=huodongList&orderByUse=1">使用数排序</a></h3>
    <h3><a href="/Ridding/backend.do?action=huodongList&orderByPicture=1">照片数排序</a></h3>
</div>

<div>
   <h3><#if topUpdateTime == -1><#else><a href="/Ridding/backend.do?action=huodongList&requestTime=${topUpdateTime!"-1"}&nextOrBefore=1">上一页</a></#if></h3>
   <h3><a href="/Ridding/backend.do?action=huodongList&requestTime=${bottomUpdateTime!"-1"}&nextOrBefore=0">下一页</a></h3>
</div>

<#if riddingList?exists>
<div>
<table border="1" cellspacing="0px" width="100%">
    <thead>
         <tr><td style="text-align: center;">id</td>
         <td style="text-align: center;">名称</td>
         <td style="text-align: center;">创建时间</td>
         <td style="text-align: center;">创建人Id</td>
         <td style="text-align: center;">创建人昵称</td>
         <td style="text-align: center;">路线封面</td>
         <td style="text-align: center;">图片列表</td>
         <td style="text-align: center;">队员数</td>
         <td style="text-align: center;">喜欢数</td>
         <td style="text-align: center;">评论数</td>
         <td style="text-align: center;">使用数</td>
         <td style="text-align: center;">关注数</td>
         <td style="text-align: center;">照片数</td>
         <td style="text-align: center;">操作</td></tr>
    </thead>
     <tbody>
    <#list riddingList as ridding>

         <tr>
            <td style="text-align: center;" width="5%"><span>${ridding.id!0}</span></td>
            <td style="text-align: center;" width="8%"><a id="huodong_name_${ridding.id!0}" href="/Ridding/backend.do?action=backendHuodong&riddingId=${ridding.id!0}">${ridding.name!""}</a></td>
            <td style="text-align: center;" width="5%"><span >${ridding.createTimeStr}</span></td>
            <td style="text-align: center;" width="5%"><span >${ridding.leaderUserId}</span></td>
            <td style="text-align: center;" width="8%">
            	<#if ridding.leaderProfile?exists>
                   <span>${ridding.leaderProfile.userName!""}</span>
				</#if> 
            </td>
            <td style="text-align: center;" width="10%"><img style="width:100px;height:100px;" src="${cfg_imageHost}${ridding.firstPicUrl!""}" id="huodong_coverImg"/></td>
            <td style="word-break:break-all; word-wrap:break-all; text-align: center; width="25%"">
              <#if ridding.riddingPictureList?exists>
                <#list ridding.riddingPictureList as picture>
                	<div class="picture">
						<img style="width:100px;height:100px;" src="${cfg_imageHost}${picture.photoUrl!""}" id="huodong_img_${picture.id!0}"/><br>
						<span>${picture.description!""}</span>
					</div>
                </#list>
              </#if> 
            </td>
            <td style="text-align: center;" width="4%">
               <div style="text-align: center;">${ridding.userCount!0}</div>
            </td>
            <td style="text-align: center;" width="4%">
               <div style="text-align: center;">${ridding.likeCount!0}</div>
            </td>
            <td style="text-align: center;" width="4%">
               <div style="text-align: center;">${ridding.commentCount!0}</div>
            </td>
            <td style="text-align: center;" width="4%">
               <div style="text-align: center;">${ridding.useCount!0}</div>
            </td>
            <td style="text-align: center;" width="4%">
               <div style="text-align: center;">${ridding.careCount!0}</div>
            </td>
            <td style="text-align: center;" width="4%">
              <div style="text-align: center;">${ridding.pictureCount!0}</div>
            </td>
            <td style="text-align: center;" width="10%"><a href="javascript:void(0);;" class="huodong_delete" data-id="${ridding.id!0}">删除</a>||<a href="javascript:void(0);;" class="huodong_recom" data-id="${ridding.id!0}">设置为推荐</a></td>
         </tr>
  
    </#list>
      </tbody>
</table>
</div>
</#if>


<#include "footer.ftl">
</body>
<script>

</script>
</html>
</#escape>
<#include "js.ftl">