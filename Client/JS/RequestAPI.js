(function (ctx) {
        var Utils             = ctx.Utils || (ctx.Utils = {});
        var CLIENT_ID         = localStorage.getItem("CLIENT_ID");
        if(CLIENT_ID == null){
            console.log("entering");
            CLIENT_ID = Utils.randomGenerator();
            localStorage.setItem("CLIENT_ID", CLIENT_ID);
        }

        var EMPTY_FUNCTION    = function(){console.log(arguments)};

        function getDefaultConfigurations(){
                return {
                    method    : 'POST',
                    timeout   : 2000,
                    onTimeout : EMPTY_FUNCTION,
                    onSuccess : EMPTY_FUNCTION,
                    onError   : EMPTY_FUNCTION,
                }
        }

        function RequestAPI(config,events){

                this.options = Utils.mixIn(config,Utils.mixIn(events,getDefaultConfigurations()));
                        
                this.xhr = new XMLHttpRequest();    
                
                this.xhr.timeout =this.options.timeout;
                
                this.xhr.onreadystatechange = this.onReadyStateChange.bind(this);
            
                this.xhr.ontimeout = this.onTimeout.bind(this);        
        }

        RequestAPI.prototype = {

                send:function(){
        
                    this.xhr.open(this.options.method,this.options.url,true);
                    this.xhr.setRequestHeader("content-type","application/x-www-form-urlencoded");
                    this.xhr.send(this.options.data);
        
                },
        
        
                onReadyStateChange:function(){
                    if(this.xhr.readyState == 4 ){
                        if(this.xhr.status==200){
                            console.log(" SUCCESS ");
                            this.options.onSuccess(JSON.parse(this.xhr.response));
                        }
                        else{
                            console.log("Error");
                            this.options.onError(JSON.parse(this.xhr.response));
                        }
                    }
                },
        
                onTimeout:function(){
                    console.log(" TIME OUT ");
                    this.options.onTimeout(this.options.data);
                },
        
                abort:function(){
                    this.xhr.abort();
                }
        }

        function RequestParams(){
                this.method = null;
                this.headers  = null;
                this.url = null;        
                this.requestBody = null;
            }
        
        
            RequestParams.prototype = {
                
                setMethodType:function(method){
                        this.method = method;
                },
        
                setRequestHeaders:function(header){
                    this.headers = header;
                },
        
                addRequestHeader:function(key,value,validate){
                    if(validate && !Utils.isUndefined(this.headers[key])){
                        throw new Error('Duplicate header')                
                    }
                    this.headers[key] = value;
                },
        
                isHeaderAvailable:function(key){
                    return !Utils.isUndefined(this.headers[key]);
                },
        
                removeHeader:function(key){
                    delete this.headers[key];
                },
        
                getAsFormData:function(){
                    /*var formdata = new FormData();
                    formdata.append("headers",JSON.stringify(this.headers));
                    formdata.append("requestBody",Utils.getQueryString(this.requestBody));
                    formdata.append("url",this.url);
                    formdata.append("clientID",CLIENT_ID);
                    formdata.append("requestMethod",this.method);
                    return formdata;*/
                    var str = {};
                    this.headers && (str.headers =  JSON.stringify(this.headers));
                    this.requestBody && (str.requestBody =  JSON.stringify(this.requestBody));
                    str.requestMethod   = this.method;
                    str.url             = this.url;
                    str.clientID        = CLIENT_ID;
                    console.log(" str : ",str);
                    return Utils.getQueryString(str);
                },
        
                setRequestBody:function(bdy){
                    this.requestBody = bdy;
                },
        
                addQueryParam:function(key,value,validate){
                    if(validate && !Utils.isUndefined(this.requestBody[key])){
                        throw new Error('Duplicate param');               
                    }
                    this.requestBody[key] = value;
                },
        
                removeQueryParam:function(key){
                    delete this.requestBody[key];
                }
            }
        
        
        
        ctx.RequestParams = RequestParams;
        ctx.RequestAPI= RequestAPI;
        ctx.CLIENT_ID = CLIENT_ID;

})(this);