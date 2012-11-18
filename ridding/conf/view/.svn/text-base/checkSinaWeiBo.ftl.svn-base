<!DOCTYPE HTML>
<#escape x as x?html>
<#assign pageName = "checkSinaWeiBo" />
<html lang="en" xmlns:og="http://opengraphprotocol.org/schema/" xmlns:fb="http://www.facebook.com/2008/fbml" dir="ltr" xmlns="http://www.w3.org/1999/xhtml">
<head profile="http://gmpg.org/xfn/11">
<meta content="text/html; charset=UTF-8" http-equiv="Content-Type">

<title>骑行app</title>
<meta content="骑行app" name="author">
<meta content="骑行app" name="publisher">
<meta content="骑行app" name="copyright">
<meta content="It's all about photography, fashion, music, lifestyle and trends. We present you talents, newcomers and professionals from all over the world. International online magazine - based in Vienna." name="description">
<meta content="C-Heads, magazine, online-magazine, ezine, photography, fashion, lifestyle, trends, art, interviews, portfolios, sedcards, agencies, events, creative, heads, representation, shootings, backstage, models, production, film, artist, network" name="keywords">
<meta content="Alle" name="audience"><meta content="en" http-equiv="content-language">
<meta content="index, follow" name="robots">
</head>
<style>
html,body,#gmap{height:100%; margin:0;}
body{font-size:83%;}
#help{padding-top:20%; text-align:center;}
</style>
<body>
<h3>
   <a href="/backend/checkUrl/?page=1&type=0&sourceType=1">非法的微博</a>
   <a href="/backend/checkUrl/?page=1&type=1&sourceType=1">已经处理的但是在map中不存在的微博</a>
</h3>
<div>
<span>总数为:${sourceTotalCount!0}</span>
</div>
<table border="1">
<thead>
<tr><td>新浪微博id</td><td>创建日期</td><td>发布用户</td><td>新浪微博内容</td><td>添加地图链接</td><td>无效链接处理</td></tr>
</thead>
<tbody>
<#if sourceList?exists>
<#list sourceList as source>
<tr id="${source.id!0}">
<td>${source.sourceId!0}</td><td>${source.createTimeString!''}</td>
<td>${source.userName!''}</td><td width="300">${source.text!''}</td>
<td><input type="text" style="width:400px;" id="input_${source.sourceId!0}"/><a  href="javascript:void(0);;" onClick="inputSubmit(${source.sourceId!0},${source.sourceType!0});return;">提交</a></td>
<td><a href="javascript:void(0);;" onClick="inputInvalided(${source.sourceId!0},${source.sourceType!0});return;"/>清理</a></td></tr>
</#list>
</#if>
</tbody>
</table>
<#include "footer.ftl">
</body>
</html>
</#escape>
<#include "js.ftl">