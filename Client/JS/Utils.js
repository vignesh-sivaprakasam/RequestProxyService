(function (ctx) {
        var Utils = Object.create(null);



        Utils.getDomById = function(domId){
                return document.getElementById(domId);
        }

        Utils.addClass = function(cls,dom){
                if(!Utils.isUndefined(dom)){
                        dom.classList.add(cls);
                }
        }
        
        Utils.removeClass = function(cls,dom){
                if(!Utils.isUndefined(dom)){
                        dom.classList.remove(cls);
                }
        }

        Utils.isUndefined = function(val){
                return val==undefined;
        }

        Utils.randomGenerator = function(){
                var resultString = '';
                var characters   = "abcdefghijklmnopqrstuvwxyz0123456789";
                for(var i=0;i<10;i++){
                        resultString += characters.charAt(Math.floor(Math.random()*characters.length));
                }
                return resultString+Date.now();
        }

        Utils.getQueryString = function(obj){
                var resultString   = "";
                var paramseparator = "&";
                if(!Utils.isUndefined(obj)){
                    for(var i in obj){
                            if(obj.hasOwnProperty(i)){
                                resultString+=encodeURIComponent(i)+"="+ encodeURIComponent(obj[i])+paramseparator;
                            }
                    }
                    resultString = resultString.slice(0, resultString.length-1);
                }
                return resultString;
        }
        
        Utils.mixIn = function(tar,source){
                for(var i in source){
                    if(source.hasOwnProperty(i)){
                        if(Utils.isUndefined(tar[i])){
                            tar[i] = source[i];
                        }
                    }
                }
                return tar;
            }

        ctx.Utils = Utils;
})(this);