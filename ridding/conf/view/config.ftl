<#-- 基础配置 -->
<#assign
    cfg_host="http://qiqunar.com.cn"
    cfg_imageHost="http://images.qiqunar.com.cn"
>
<script type="text/javascript">
    cfg_host='${cfg_host}';
    cfg_imageHost='${cfg_imageHost}';
    UD={
       hostUserId:'${hostUserId!0}',
       hostSAvatorUrl:'${hostSAvatorUrl!""}',
       hostNickname:'${hostNickname!""}',
       visitUserId:'${visitUserId!0}',
       visitSAvatorUrl:'${visitSAvatorUrl!""}',
       visitBAvatorUrl:'${visitBAvatorUrl!""}'
    };
    
</script>