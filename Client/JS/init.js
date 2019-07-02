(function (ctx) {
        var templateId = "keyValueTemplate";
        var Utils      = ctx.Utils;
        var Template   = ctx.Template;

        ctx.domCache   = {};

        function cacheDOM(){
            domCache.submitBtn           = document.getElementById("send");

            domCache.inpUrl              = document.getElementById("url");
            domCache.reqType             = document.getElementById("requestMethod");
            domCache.clientID            = document.getElementById("clientID");
            domCache.clientID.value      = CLIENT_ID;
            domCache.generateID          = document.getElementById("idGenerator");

            domCache.addHeader           = document.getElementById("addHeader");
            domCache.addBody             = document.getElementById("addBody");

            domCache.requestBodyHolder   = document.getElementById("requestBodyHolder");
            domCache.requestHeaderHolder = document.getElementById("requestHeaderHolder");

            domCache.statusCode          = document.getElementById("statusCode");
            domCache.responseText        = document.getElementById("responseText");
        }

        function init(){
                cacheDOM();
                bindEvents();
        }
        
        
        function bindEvents(){
                domCache.addHeader.addEventListener("click",_addHeader);
                domCache.addBody.addEventListener("click",_addBody);
                domCache.submitBtn.addEventListener("click",_sendRequest);
                domCache.generateID.addEventListener("click", _generateID);
        }

        function _generateID() {
            CLIENT_ID = Utils.randomGenerator();
            domCache.clientID.value = CLIENT_ID;
            localStorage.setItem("CLIENT_ID", CLIENT_ID);
        }

        function _addHeader(){
                var dom = Template.getTemplate(templateId);
                domCache.requestHeaderHolder.append(dom);
        }
        
        function _addBody(){
                var dom = Template.getTemplate(templateId);
                domCache.requestBodyHolder.append(dom);
        }

        function _sendRequest(){
                if(domCache.inpUrl.value.trim()==""){
                    alert("Enter valid URL");
                    return;
                }
        
                var headers   = _getParamList(domCache.requestHeaderHolder);
                var body      = _getParamList(domCache.requestBodyHolder);
        
                var requestParams = new ctx.RequestParams();
                requestParams.setMethodType(domCache.reqType.options[domCache.reqType.selectedIndex].value);
                requestParams.setRequestHeaders(headers);
                requestParams.setRequestBody(body);
                requestParams.url = domCache.inpUrl.value;
                console.log(" ",requestParams.getAsFormData());
                reqAPI = new RequestAPI({
                    url   : 'http://localhost:8080/Sample_project/ApiServlet',
                    data  : requestParams.getAsFormData()
                },{
                    onSuccess:function(resp){
                        console.log("Success",resp);
                        domCache.statusCode.textContent   = resp.status;
                        domCache.responseText.textContent = resp.responseText;
                        reqAPI = null;
                    },
                    onError:function(){
                        console.log("Error");
                    }
                });
        
                reqAPI.send();
            }
           
        function _getParamList(dom){
                var keys    = dom.querySelectorAll(".keys");
                var values  = dom.querySelectorAll(".values");
                var result  = null;
                for(var i = 0; i < keys.length; i++){
                    if(keys[i].value != "" && values[i].value != ""){
                        if(result == null){
                            result = {};
                        }
                    	result[keys[i].value] = values[i].value;
                    }
                }
                return result;
        }

        init();
})(this);