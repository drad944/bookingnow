var ContentContainer = {
	
	create: function(config){
		var parent = config.parent;
		delete config.parent;
		this.contentDiv = document.createElement("DIV");
		AppUtil.setStyle(this.contentDiv, {width: config.width, height: config.height});
		this.contentDiv.className = "ContentDiv";
		this.viewWidth = config.width;
		this.viewHeight = config.height;
		this.viewsCount = config.views.length;
		this.createViewStack(config.views);
		this.addCallBack();
		parent.appendChild(this.contentDiv);
	},

	addCallBack: function(){
		var me = this;
		this.contentDiv.addEventListener("touchstart", function(event){
			if(event.touches.length > 0) {
				var touch = event.touches[0];
				me.startX = touch.pageX;
			}
		}, false);
		this.contentDiv.addEventListener("touchend", function(event){
			if(event.changedTouches.length > 0){
			    var touch = event.changedTouches[0];
			    var value = 0;
			    if(touch.pageX > me.startX){
			        //move right
			        value = touch.pageX - me.startX;
			        if(value > me.viewWidth/5){
			        	me.updateView(me.currentIdx - 1);
			        } else {
			        	me.updateView(me.currentIdx);
			        }
			    } else if(touch.pageX < me.startX){
			        //move left
			        value = me.startX - touch.pageX;
			        if(value > me.viewWidth/5){
			        	me.updateView(me.currentIdx + 1);
			        } else {
			        	me.updateView(me.currentIdx);
			        }
			    }
			}
		}, false);
	},
	
	createViewStack: function(viewStack){
		var table = document.createElement('table');
		var tr = document.createElement('tr');
		table.appendChild(tr);
		
		var td;	
		for(var i=0 ; i < viewStack.length; i++){
			var viewtable = document.createElement('table');
			//leave 30px for the horizontal scroll bar
			AppUtil.setStyle(viewtable, {width: this.viewWidth - 2, height: this.viewHeight - 30});
			viewtable.className = "ViewTable";
			var viewtr =  document.createElement('tr');
			var viewtd = document.createElement('td');
			var viewdiv = document.createElement('div');
			viewdiv.className = "ViewDiv";
			viewdiv.innerHTML = viewStack[i].content;
			viewtd.appendChild(viewdiv);
			viewtr.appendChild(viewtd);
			viewtable.appendChild(viewtr);
			td = document.createElement('td');
			td.appendChild(viewtable);
			tr.appendChild(td);
		}
		this.contentDiv.appendChild(table);
		this.viewsTable = table;
		this.updateView(0);
		//this.currentIdx = 0;	
	},

	moveViewGently: function (begin, end){
	    if(begin == end){
	    	return;
	    }
	    var step = 0;
	    if(begin > end){
	    	step = (begin - end) % 50;
	    	if(step == 0)
	    		step = 50;
	    	 begin -= step;
	    } else {
	    	step = (end - begin) % 50;
	        if(step == 0)
	            step = 50;
	    	begin += step;
	    }
	    this.contentDiv.scrollLeft = begin;
		setTimeout("ContentContainer.moveViewGently("+begin+", "+end+")", 10);
	},

	calculateViewPosition: function(index){
		return this.viewsTable.firstChild.children[index].offsetLeft;
	},
	
	updateView: function(index){
		if(index < 0 || index > this.viewsCount){
			return;
		}
		//refine the calculation by search the view start point
		//if(end > 0) end = end - 4;
		this.moveViewGently(this.contentDiv.scrollLeft, this.calculateViewPosition(index));
		this.currentIdx = index;
	}
};