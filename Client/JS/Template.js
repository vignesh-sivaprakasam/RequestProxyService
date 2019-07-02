(function (ctx) {
        var _templateCaches   = Object.create(null);
        var Template          = Object.create(null);

        Template.getTemplate = function (name) {
		if (name !== null) {
			var tmpl = _templateCaches[name];
			if (!tmpl) {
				_templateCaches[name] = document.getElementById(name);
				tmpl = _templateCaches[name];
			}
			return _cloneTemplate(tmpl);						
		}
        }
        
        function _cloneTemplate(element) {					
		var el =  element.content.cloneNode(true);
		return el.firstElementChild || el.firstChild;
        }
        
        ctx.Template = Template;
})(this);