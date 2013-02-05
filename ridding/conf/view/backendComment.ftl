<!DOCTYPE HTML>
<#escape x as x?html>
<#assign pageName = "backendComment" />
<#include "head.ftl">
<style>
html,body,#gmap{height:100%; margin:0;}
body{font-size:83%;}
#help{padding-top:20%; text-align:center;}
</style>
<body>
<h3 style="font-size: 20px;color: red;"><a href="/backend/index/">返回</a></h3><br>

<div style="">
	<span>输入骑行Id:</span>
    <input type="text" id="search_comment_by_riddingId" value=""/>
</div><br>

<#if riddingCommentList?exists>
<div>
<table border="1px" cellspacing="0px" width="80%">
	<thead>
		<tr>
			<th>评论ID</th>
			<th>评论人</th>
			<th>评论类型</th>
			<th>回复ID</th>
			<th>回复时间</th>
			<th>内容</th>
			<th>操作ID</th>
		</tr>
	</thead>
	<#list riddingCommentList as riddingComment>
		<tbody>
			<tr>
				<td style="text-align: center;" width="10%">
					<div style="text-align: center;">${riddingComment.id!0}</div>
				</td>
				<td style="text-align: center;" width="10%">
					<div style="text-align: center;">${riddingComment.userProfile.userName!""}</div>
				</td>
				<td style="text-align: center;" width="10%">
					<div style="text-align: center;">
						<#if riddingComment.commentType==0>
							<div style="text-align: center;">
								<span>评论</span>
							</div>
						<#else>
							<div style="text-align: center;">
								<span>评论回复</span>
							</div>
						</#if>
					</div>
				</td>
				<td style="text-align: center;" width="10%">
					<div style="text-align: center;">${riddingComment.replyId!0}</div>
				</td>
				<td style="text-align: center;" width="10%">
					<div style="text-align: center;">${riddingComment.CreateTime!0}</div>
				</td>
				<td width="40%" style="word-break:break-all; word-wrap:break-all; text-align: center;">
					<div>${riddingComment.text!""}</div>
				</td>
				<td style="text-align: center;" width="10%">
					<a href="">删除</a>
				</td>
			</tr>
		</tbody>
	</#list>
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