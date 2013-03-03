<!DOCTYPE HTML>
<#escape x as x?html>
<#assign pageName = "backendVip" />
<#include "head.ftl">
<style type="text/css">
html,body,#gmap{height:100%; margin:0;}
body{font-size:83%;}
#help{padding-top:20%; text-align:center;}
img{max-width: 100%;max-height: 80%}
div.outset {border-style: none;width: 20%;height: 300px;float:left;clean:both}
</style>
	
<body>
<h3 style="font-size: 20px;color: red;"><a href="/backend/index/">返回</a></h3>

<div>
    <h3>taobaoCode搜索:<input type="text" id="taobaoCode"/><input type="button" value="搜索" id="codeSearch"/></h3>
    
    <div >
         <p>用户Id:<span id="searchResult_userId" style="color: red;"></span></p>
         <p>用户名:<span id="searchResult_userName" style="color: red;"></span></p>
         <table class="topmargin" border="1">
            <thead>
                <tr><td>id</td><td>类型</td><td>起始日期</td><td>失效日期</td><td>操作</td></tr>
            </thead>
            <tbody id="searchResult_tbody"> 
               
            </tbody>
         </table>
    </div>
</div>


<div>
    <h3>淘宝Code:<input type="text" id="add_code"/></h3>
    <h3>类型:<input type="text" id="add_type"/></h3>
    <h3>时间长度:<input type="text" id="add_long"/></h3>
    <input type="button" id="add_btn" value="添加"/>
</div>
<#include "footer.ftl">
</body>
<script>

</script>
</html>
</#escape>
<#include "js.ftl">