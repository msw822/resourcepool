function login(url,loginCmdText){
	
    return $.ajax({
         url: url,
         type: "GET",
         async: false,
         data: "command=login" + loginCmdText + "&response=json",
         dataType: 'json',
         cache: false,
         error: function(data) {
         	var loginresponse = data.responseText;
         	var errorCode = data.responseText.errorcode;
         	var errortext = data.responseText.errortext; 
         	return false;
         },
         success: function(json) {
         	
         	var loginresponse = json.loginresponse;
         	var sessionKey =json.loginresponse.sessionkey;
         	var username = json.loginresponse.username;
         	if(null!=sessionKey && ""!=sessionKey)
         	{
         		
           	    g_mySession = $.cookie('JSESSIONID');
                 g_sessionKey = encodeURIComponent(loginresponse.sessionkey);
                 g_role = loginresponse.type;
                 g_username = loginresponse.username;
                 g_userid = loginresponse.userid;
                 g_account = loginresponse.account;
                 g_domainid = loginresponse.domainid;
                 g_timezone = loginresponse.timezone;
                 g_timezoneoffset = loginresponse.timezoneoffset;
                 g_userfullname = loginresponse.firstname + ' ' + loginresponse.lastname;

                 $.cookie('sessionKey', g_sessionKey, {
                     expires: 1
                 });
                 $.cookie('username', g_username, {
                     expires: 1
                 });
                 $.cookie('account', g_account, {
                     expires: 1
                 });
                 $.cookie('domainid', g_domainid, {
                     expires: 1
                 });
                 $.cookie('role', g_role, {
                     expires: 1
                 });
                 $.cookie('timezoneoffset', g_timezoneoffset, {
                     expires: 1
                 });
                 $.cookie('timezone', g_timezone, {
                     expires: 1
                 });
                 $.cookie('userfullname', g_userfullname, {
                     expires: 1
                 });
                 $.cookie('userid', g_userid, {
                     expires: 1
                 });
                 return true;
         	}
         }
    });
	
	
	
	
	
} 