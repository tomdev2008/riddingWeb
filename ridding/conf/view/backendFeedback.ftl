<!DOCTYPE HTML>
<#escape x as x?html>
<#assign pageName = "feedback" />
<#include "head.ftl">
<style>
html,body,#gmap{height:100%; margin:0;}
body{font-size:83%;}
#help{padding-top:20%; text-align:center;}
</style>
<body>
<h3 style="font-size: 20px;color: red;"><a href="/backend/index/">返回</a></h3><br>

<div style="">
	<span>输入反馈Id:</span>
    <input type="text" id="search_feedback_by_id" value=""/>
</div><br>

<#if feedbackList?exists>
<div>
<table border="1px" cellspacing="0px" width="80%">
	<thead>
		<tr>
			<th>反馈ID</th>
			<th>反馈人</th>
			<th>QQ</th>
			<th>Email</th>
			<th>反馈时间</th>
			<th>反馈内容</th>
			<th>处理状态</th>
			<th>回复时间</th>
			<th>回复内容</th>
			<th>输入回复</th>
			<th>操作</th>
		</tr>
	</thead>
	<#list feedbackList as feedback>
		<tbody>
			<tr>
				<td style="text-align: center;" width="10%">
					<div style="text-align: center;" id="reply_id">${feedback.id!0}</div>
				</td>
				<td style="text-align: center;" width="10%">
					<div style="text-align: center;" id="reply_userId">${feedback.userId!0}</div>
				</td>
				<td style="text-align: center;" width="10%">
					<div style="text-align: center;">${feedback.userQQ!0}</div>
				</td>
				<td style="text-align: center;" width="10%">
					<div style="text-align: center;">${feedback.userMail!""}</div>
				</td>
				<td style="text-align: center;" width="10%">
					<div style="text-align: center;">${feedback.createTimeStr!0}</div>
				</td>
				<td style="text-align: center;" width="10%">
					<div style="text-align: center;">${feedback.description!""}</div>
				</td>
				<td style="text-align: center;" width="10%">
					<div style="text-align: center;">
						<span style="color: red;"><#if feedback.status == 1>已回复<#else>未回复</#if></span>
					</div>
				</td>
				<td style="text-align: center;" width="10%">
					<div style="text-align: center;">${feedback.replyTime!0}</div>
				</td>
				<td style="text-align: center;" width="10%">
					<div style="text-align: center;" id="reply_text">${feedback.reply!""}</div>
				</td>
				<td width="">
					<input type="text" id="feedback_reply" value=""/>
				</td>
				<td style="text-align: center;" width="10%">
					<input type="button" id="reply" value="回复"/>
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