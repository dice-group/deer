(window.webpackJsonp=window.webpackJsonp||[]).push([[0],{53:function(e,t,a){e.exports=a(95)},59:function(e,t,a){},60:function(e,t,a){},63:function(e,t,a){},65:function(e,t,a){},71:function(e,t){},73:function(e,t){},90:function(e,t,a){e.exports=a.p+"static/media/deer-logo.b55379b3.svg"},91:function(e,t,a){},95:function(e,t,a){"use strict";a.r(t);var s=a(1),n=a.n(s),r=a(14),i=a.n(r),l=a(10),o=a(116),d=(a(58),a(59),a(60),a(61),a(62),a(47)),c=a(113),p=a(114),h=a(115),u=(a(63),a(48)),m=a.n(u);a(65);var g=class extends n.a.Component{constructor(e){super(e);var t=this;this.linkWidget=this.addWidget("button","read more",this.linkName,function(e){e&&window.open(t.linkName,"_blank")}),this.widgets_start_y=70,this.widgets_up=!1,this.onDrawForeground=((e,t)=>{if(!this.flags.collapsed){e.font="12px Arial";var a=null;this.message[0]&&(a=this.message[0].replace(/"/g,"")),a=a||"Description of the node ...Continue description of the node ...";for(var s=this.splitStringForCanvas(a).split("\n"),n=0;n<s.length;n++)e.fillText(s[n],10,55+12*n)}})}splitStringForCanvas(e){var t="",a=27;this.props.includes("Operator")&&(a=49);for(var s=new RegExp(".{1,"+a+"}","g"),n=e.match(s),r=0;r<n.length;r++)t+=n[r].trim()+"\n";return t}render(){return n.a.createElement("div",null)}},f=a(97),w=a(98),E=a(99),v=a(100),b=a(101),x=a(102);var y=class extends n.a.Component{constructor(e){super(e),this.changeInput=((e,t)=>{var a=Object.assign({},this.props.panelData);a.properties[t]=e.target.value,this.props.updateParentPanelData(a,this.props.panelData.numNodeType)}),this.changeInputWithRadio=((e,t)=>{this.changeInput(e,t);var a=Object.assign({},this.props.panelData),s={},n=this.props.panelData.xoneProperties.filter(e=>void 0!==e&&e!==t);Object.keys(a.properties).forEach(e=>{n.forEach(t=>{s[e]=t===e?"":a.properties[e]})}),a.properties=s,this.props.updateParentPanelData(a,this.props.panelData.numNodeType)}),this.changeRadio=((e,t)=>{e.target.value="type here",this.changeInputWithRadio(e,t)})}render(){var e=this.props.panelData.xoneProperties.filter(e=>void 0!==e),t=Object.keys(this.props.panelData.properties).filter(t=>!e.includes(t));return n.a.createElement("div",null,n.a.createElement(f.a,null,n.a.createElement("div",{className:"numbers"},n.a.createElement(w.a,{tag:"p"},this.props.panelData.nodePath.split("/")[1]),n.a.createElement("h4",null,this.props.panelData.nodePath)),n.a.createElement(E.a,null,n.a.createElement("p",null,"Properties"),t.map(e=>n.a.createElement(s.Fragment,{key:e},n.a.createElement(v.a,null,n.a.createElement("label",null,e),n.a.createElement(b.a,{placeholder:e,type:"text",value:this.props.panelData.properties[e],onChange:t=>this.changeInput(t,e)})))),e.map(e=>n.a.createElement(v.a,{check:!0,key:e},n.a.createElement("label",null,e),n.a.createElement(x.a,{check:!0},n.a.createElement(b.a,{type:"radio",checked:0!==this.props.panelData.properties[e].length,onChange:t=>this.changeRadio(t,e),name:"radio1"}),n.a.createElement(b.a,{placeholder:e,type:"text",value:this.props.panelData.properties[e],onChange:t=>this.changeInputWithRadio(t,e)})))))))}},N=a(117),P=a(103),C=a(104),k=a(105),D=a(106),S=a(107),F=a(108),O=a(109),I=a(118),R=a(110),j=a(111),L=a(112),T=a(94),M=T.DataFactory,q=M.namedNode,B=M.literal,_=M.defaultGraph,W=window.location.href,z=(window.location.hostname,window.location.port,window.LiteGraph);z.registered_node_types={},z.searchbox_extras={};var A=[{path:"/dashboard",name:"Dashboard",icon:"nc-icon nc-bank",component:class extends n.a.Component{constructor(e){super(e),this.updateParentPanelData=((e,t)=>{var a=Object.assign([],this.state.panelData),s=a.findIndex(e=>t===e.numNodeType);a[s]=e,this.setState({panelData:a})}),this.getBaseUrl=(()=>new RegExp(/^.*\//).exec(window.location.href)),this.callbackFunction=(e=>{this.setState({formProperties:e})}),this.resizeWindowFunc=(()=>{var e=document.getElementById("mycanvas"),t=document.getElementById("parentCanvas");e.width=t.offsetWidth,e.height=t.offsetHeight,null!=this.state.graphCanvas&&this.state.graphCanvas.resize()}),this.getPropertyFirst=(e=>{e.forEach(e=>{var t=this.state.fullContent.filter(t=>t.subject.id.includes(e.object.id));if(t[0]&&!t[0].object.id.includes("_:n")){var a=this.state.tempXoneNodeParams;a.push(t[0].object.id),this.setState({tempXoneNodeParams:a})}!e.object.id.includes("nil")&&e.predicate.id.includes("rest")&&this.getPropertyFirst(t)})}),this.getPropertiesForNode=(e=>{this.setState({tempXoneNodeParams:[]});var t=this.state.fullContent.filter(t=>t.subject.id.includes(e)&&t.predicate.id.includes("property")).map(e=>e.object.id),a=this.state.fullContent.filter(t=>t.subject.id.includes(e)&&t.predicate.id.includes("xone")),s=[];return a.forEach(e=>{var t=this.state.fullContent.filter(t=>t.subject.id.includes(e.object.id));this.getPropertyFirst(t),s=this.state.tempXoneNodeParams}),{basicProps:t,xone:s}}),this.getPropertyName=(e=>{var t=e.split("_");return t.length>2?t[t.length-1]:t[1]}),this.getInputPorts=(e=>this.state.fullContent.filter(t=>t.subject.id.includes(e)&&t.predicate.id.includes("maxInPorts")).map(e=>e.object.id)),this.getOutputPorts=(e=>this.state.fullContent.filter(t=>t.subject.id.includes(e)&&t.predicate.id.includes("maxOutPorts")).map(e=>e.object.id)),this.getMessage=(e=>this.state.fullContent.filter(t=>t.subject.id.includes(e)&&t.predicate.id.includes("comment")).map(e=>e.object.id)),this.getUrlForTheNode=(e=>this.state.fullContent.filter(t=>t.subject.id.includes(e)&&t.predicate.id.includes("seeAlso")).map(e=>e.object.id)),this.initializeNode=(e=>{var t=this.getPropertiesForNode(e),a=this.getInputPorts(e)[0],s=this.getOutputPorts(e)[0],n=a.match(/\d+/)[0],r=s.match(/\d+/)[0],i=this.getMessage(e),l=this.getUrlForTheNode(e),o=null,d=this,c={name:""},p=t.basicProps.map(e=>this.getPropertyName(e));if(p.forEach(e=>{c[e]=""}),p=t.xone.map(e=>this.getPropertyName(e)),o=p,p.forEach(e=>{c[e]=""}),delete c.undefined,e.includes("Operator")){class t extends g{constructor(t){super(t);for(var a=0;a<n;a++)this.addInput("input","text");for(var s=0;s<r;s++)this.addOutput("output","text");this.message=i,this.linkName=l,this.onDblClick=(t=>{d.showProperies("Operator/",t,e,c,o)})}}t.title=e,t.size=[320,100],t.color="#664d00",t.bgcolor="#8c6a00",z.registerNodeType("Operator/"+e,t)}else if(e.includes("Reader")){class t extends g{constructor(t){super(t),this.addOutput("output","text"),this.message=i,this.linkName=l,this.onDblClick=(t=>{d.showProperies("Reader/",t,e,c,o)})}}t.title=e,t.size=[180,100],t.color="#223322",t.bgcolor="#335533",z.registerNodeType("Reader/"+e,t)}else{class t extends g{constructor(t){super(t),this.addInput("input","text"),this.message=i,this.linkName=l,this.onDblClick=(t=>{d.showProperies("Writer/",t,e,c,o)})}}t.title=e,t.size=[180,100],t.color="#223322",t.bgcolor="#335533",z.registerNodeType("Writer/"+e,t)}}),this.showProperies=((e,t,a,s,n)=>{var r=Object.assign({},s),i=Object.assign([],this.state.panelData);i=i.map(e=>{var t=Object.assign({},e);return t.showPanel=!1,t}),this.state.panelData.find(s=>s.numNodeType===t.target.data.current_node.id&&s.nodePath===e+a)?(i[this.state.panelData.findIndex(s=>s.nodePath===e+a&&s.numNodeType===t.target.data.current_node.id)].showPanel=!0,this.setState({panelData:i})):(i.push({numNodeType:t.target.data.current_node.id,nodePath:e+a,properties:r,xoneProperties:n,showPanel:!0}),this.setState({panelData:i}))}),this.showOrDisableXoneProperties=((e,t,a)=>{if(console.log(a),a.includes(t)){var s=a.filter(e=>e!==t);a.forEach(e=>{document.querySelectorAll("[data-property="+e+"]")[0].classList.remove("disabledDiv")}),s.forEach(t=>{document.querySelectorAll("[data-property="+t+"]")[0].classList.add("disabledDiv"),document.querySelectorAll("[data-property="+t+"]")[0].getElementsByClassName("property_value")[0].innerHTML="",delete e[t]})}}),this.showNode=(e=>{if(e.predicate.id.includes("targetClass")){var t=e.object.id.split("https://w3id.org/deer/")[1],a=this.state.nodesArray;t&&(a.add(t),this.initializeNode(t)),this.setState({nodesArray:a})}}),this.toggleDropdown=(()=>{this.setState({show:!this.state.show})}),this.getInputLink=(e=>{for(var t=0;t<e.inputs.length;t++){console.log("i = "+t);var a=e.inputs[t].link;console.log(a);var s=this.state.graph.links[a];if(s){var n=this.state.graph.getNodeById(s.origin_id),r=this.state.inputPorts;r.push(n),this.setState({inputPorts:r})}}}),this.saveConfig=(()=>{var e=this.state.graph.serialize(),t=(M.quad(q("https://ruben.verborgh.org/profile/#me"),q("http://xmlns.com/foaf/0.1/givenName"),B("Ruben","en"),_()),new T.Writer({prefixes:this.state.prefixes},{format:"N-Triples"}));new T.Parser({format:"N3",blankNodePrefix:""}),e.nodes.map((e,a)=>{if(e.properties=this.state.panelData.filter(t=>t.numNodeType===e.id&&t.nodePath===e.type)[0].properties,t.addQuad(q("urn:example:demo/"+e.properties.name),q("http://www.w3.org/1999/02/22-rdf-syntax-ns#type"),q("https://w3id.org/deer/"+e.type.split("/")[1])),e.inputs){for(var s=0;s<e.inputs.length;s++)if(e.inputs[s].link){var n=e.inputs[s].link;console.log(n);var r=this.state.graph.links[n];if(r){var i=this.state.graph.getNodeById(r.origin_id),l=this.state.inputPorts;l.push(i),this.setState({inputPorts:l,inputLinkId:n})}}console.log(this.state.inputPorts);var o=this.state.inputPorts.map((e,a)=>t.blank([{predicate:q("https://w3id.org/fcage/fromNode"),object:q("urn:example:demo/"+e.properties.name)},{predicate:q("https://w3id.org/fcage/fromPort"),object:B(a)}]));t.addQuad(q("urn:example:demo/"+e.properties.name),q("https://w3id.org/fcage/hasInput"),t.list(o)),this.setState({inputPorts:[],inputLinkId:""})}console.log(e);var d=e.properties;if("operation"in d){var c=this.addBlankNodes("operation",d);t.addQuad(q("urn:example:demo/"+d.name),q("https://w3id.org/deer/operation"),t.blank(c))}else if("selector"in d){var p=this.addBlankNodes("selector",d);t.addQuad(q("urn:example:demo/"+d.name),q("https://w3id.org/deer/selector"),t.blank(p))}else for(var h in d)"name"!==h&&d[h].length&&(d[h].includes("http")?t.addQuad(q("urn:example:demo/"+e.properties.name),q("https://w3id.org/deer/"+h),q(d[h])):t.addQuad(q("urn:example:demo/"+e.properties.name),q("https://w3id.org/deer/"+h),B(d[h])))}),t.end((e,t)=>{console.log(t),this.submitConfig(t),t=""})}),this.addBlankNodes=((e,t)=>{var a=[];for(var s in t)"name"!==s&&s!==e&&t[s].length&&a.push({predicate:q("https://w3id.org/deer/"+s),object:q(t[s])});return a}),this.downloadResults=(e=>{fetch(W+"/result/"+this.state.requestID+"/"+this.state.availableFiles[e]).then(t=>{t.blob().then(t=>{var a=window.URL.createObjectURL(t),s=document.createElement("a");s.href=a,s.download=this.state.availableFiles[e],s.click()})})}),this.submitConfig=(e=>{var t=new Blob([e],{type:"text/ttl"}),a=new File([t],"config.ttl"),s=new FormData;(s=this.uploadFiles()).append("config",a),fetch(W+"/submit",{method:"POST",body:s}).then(e=>e.json()).then(e=>{e.error&&-1===e.error.code?this.setState({visible:!0}):e.requestId&&(this.setState({requestID:e.requestId,showLogButton:!0}),this.interval=setInterval(this.getStatusForRequest,1e3))})}),this.getStatusForRequest=(()=>{fetch(W+"/status/"+this.state.requestID).then(function(e){return e.json()}).then(e=>{2===e.status.code&&(clearInterval(this.interval),this.setState({requestCompleteModal:!0,showConfigButton:!0}),this.getResults())})}),this.getResults=(()=>{fetch(W+"/results/"+this.state.requestID).then(function(e){return e.json()}).then(e=>{console.log(e.availableFiles),this.setState({availableFiles:e.availableFiles})})}),this.showLogs=(()=>{fetch(W+"/logs/"+this.state.requestID).then(function(e){var t=document.getElementById("downloadlink");t.href=e.url,t.click()})}),this.toggle=(()=>{this.setState({visible:!this.state.visible})}),this.getFilteredOptions=(e=>{this.setState({afterFilteredSuggestions:this.state.prefixOptions.filter(t=>t.toLowerCase().includes(e.target.value.toLowerCase())),userInput:e.target.value})}),this.selectedPrefix=(e=>{this.setState({afterFilteredSuggestions:[],namespace:this.state.contexts[e.target.value],userInput:e.target.value,isDisabled:!1})}),this.handleNamespaceChange=(e=>{this.setState({namespace:e.target.value,isDisabled:!1})}),this.addNewPrefixes=(e=>{this.setState({addNewPrefixes:this.state.addNewPrefixes.concat(this.state.userInput)}),this.state.prefixes[this.state.userInput]=this.state.namespace}),this.saveResults=(()=>{this.setState({showResultModal:!this.state.showResultModal})}),this.toggleResultModal=(()=>{this.setState({showResultModal:!this.state.showResultModal})}),this.toggleRequestCompleteModal=(()=>{this.setState({requestCompleteModal:!this.state.requestCompleteModal})}),this.uploadFiles=(()=>{for(var e=this.state.selectedFiles,t=new FormData,a=0;a<e.length;a++)t.append("file",e[a]);return t}),this.onSelectedFiles=(e=>{this.setState({selectedFiles:e.target.files})}),this.state={graph:new z.LGraph,graphCanvas:null,outputLinks:[],prefixOptions:[],config:"",isModalOpen:!1,namespace:"",userInput:"",afterFilteredSuggestions:[],addNewPrefixes:[],showForm:!1,showComponent:"",node:"",isDisabled:!1,nodeArr:[],file:"",visible:!1,requestID:"",showResultModal:!1,requestCompleteModal:!1,showConfigButton:!1,availableFiles:[],showLogButton:!1,formProperties:"",input1:"",input2:"",prefixes:{example:"urn:example:demo/",foaf:"http://xmlns.com/foaf/0.1/",dbpedia:"http://dbpedia.org/resource/",deer:"https://w3id.org/deer/",fcage:"https://w3id.org/fcage/",geo:"http://www.w3.org/2003/01/geo/wgs84_pos#",owl:"http://www.w3.org/2002/07/owl#",rdf:"http://www.w3.org/1999/02/22-rdf-syntax-ns#",rdfs:"http://www.w3.org/2000/01/rdf-schema#",xsd:"http://www.w3.org/2001/XMLSchema#"},nodesArray:new Set,quads:[],tempXoneNodeParams:[],fullContent:null,inputPorts:[],inputLinkId:"",selectedFiles:[],panelData:[]}}componentWillUnmount(){window.removeEventListener("resize",this.resizeWindowFunc)}componentDidMount(){this.resizeWindowFunc(),window.addEventListener("resize",this.resizeWindowFunc);var e=new z.LGraphCanvas("#mycanvas",this.state.graph);this.setState({graphCanvas:e}),this.state.graph.start(),e.show_info=!1,fetch("http://prefix.cc/context").then(function(e){return e.json()}).then(e=>{this.setState({contexts:e["@context"],prefixOptions:Object.keys(e["@context"])})}),""!==this.state.userInput&&""!==this.state.namespace||this.setState({isDisabled:!0});var t=new T.Parser;fetch(W+"/shapes").then(function(e){return e.text()}).then(e=>{var a=(new T.Parser).parse(e);this.setState({fullContent:a}),t.parse(e,(e,t,a)=>{t&&this.showNode(t)})})}render(){return m.a.map(this.state.prefixOptions,(e,t)=>({key:e,text:e,value:e})),n.a.createElement("div",{className:"content"},n.a.createElement(N.a,{isOpen:this.state.visible,toggle:this.toggle},n.a.createElement(P.a,{toggle:this.toggle},"Incorrect Configuration"),n.a.createElement(C.a,null,"Please input all the required fields and connections.")),n.a.createElement(N.a,{isOpen:this.state.requestCompleteModal,toggle:this.toggleRequestCompleteModal},n.a.createElement(P.a,{toggle:this.toggleRequestCompleteModal},"Status"),n.a.createElement(C.a,null,"The results are ready. You can download them by clicking 'Show Results'.")),n.a.createElement(N.a,{isOpen:this.state.showResultModal,toggle:this.toggleResultModal},n.a.createElement(P.a,{toggle:this.toggleResultModal},"Results"),n.a.createElement(C.a,null,n.a.createElement(k.a,null,n.a.createElement("thead",null,n.a.createElement("tr",null,n.a.createElement("th",null,"File"),n.a.createElement("th",null,"Download"))),n.a.createElement("tbody",null,this.state.availableFiles.map((e,t)=>n.a.createElement("tr",{key:t},n.a.createElement("td",null,e),n.a.createElement("td",null,n.a.createElement(D.a,{onClick:()=>this.downloadResults(t)},n.a.createElement("i",{className:"fa fa-download",style:{color:"white"}})," ","Download")))))))),n.a.createElement(S.a,null,n.a.createElement(F.a,{lg:"9",md:"9",sm:"9"},n.a.createElement(f.a,{className:"card-stats"},n.a.createElement("div",{className:"numbers"},n.a.createElement(w.a,{tag:"p"},"Prefixes")),n.a.createElement(E.a,null,n.a.createElement(S.a,null,n.a.createElement(F.a,{md:"12",xs:"12"},n.a.createElement(O.a,null,n.a.createElement(S.a,null,n.a.createElement(F.a,{md:"4"},n.a.createElement(s.Fragment,null,n.a.createElement(v.a,{className:"dropdown"},n.a.createElement("label",null,"Label"),n.a.createElement(b.a,{placeholder:"Label",type:"text",value:this.state.userInput,onChange:this.getFilteredOptions}),n.a.createElement(I.a,{className:"dropdown-content",toggle:this.toggleDropdown},this.state.afterFilteredSuggestions.map((e,t)=>n.a.createElement(R.a,{className:"dropdown-item",value:e,onClick:this.selectedPrefix},e)))))),n.a.createElement(F.a,{md:"4"},n.a.createElement(v.a,null,n.a.createElement("label",null,"Namespace"),n.a.createElement(b.a,{placeholder:"Namespace",type:"text",value:this.state.namespace,onChange:this.handleNamespaceChange}))),n.a.createElement(F.a,{md:"4"},n.a.createElement(D.a,{disabled:this.state.isDisabled,className:"btn-round prefixBtn",color:"primary",onClick:this.addNewPrefixes},"Add prefix"))))))),n.a.createElement(j.a,null,n.a.createElement("hr",null),n.a.createElement("div",{className:"stats"},this.state.addNewPrefixes.map(e=>n.a.createElement(L.a,{variant:"light"}," ",e)))))),n.a.createElement(F.a,{lg:"3",md:"3",sm:"3"},n.a.createElement(f.a,{className:"card-stats"},n.a.createElement("div",{className:"numbers"},n.a.createElement(w.a,{tag:"p"},"Attach files")),n.a.createElement(E.a,null,n.a.createElement(S.a,null,n.a.createElement(F.a,{md:"8"},n.a.createElement(b.a,{className:"file inputFile",multiple:!0,type:"file",name:"file",id:"exampleFile",onChange:this.onSelectedFiles})))),n.a.createElement("hr",null),n.a.createElement(j.a,null)))),n.a.createElement(S.a,null,n.a.createElement(F.a,{md:"9"},n.a.createElement(f.a,null,n.a.createElement("div",{className:"numbers"},n.a.createElement(w.a,{tag:"p"},"Graph")),n.a.createElement(E.a,null,n.a.createElement("div",{id:"parentCanvas",className:"litegraph litegraph-editor"},n.a.createElement("canvas",{id:"mycanvas",height:"600",width:"1000"})," ")),n.a.createElement(j.a,null,n.a.createElement("hr",null),n.a.createElement("div",{className:"stats"},n.a.createElement(D.a,{onClick:this.saveConfig},n.a.createElement("i",{className:"fa fa-cog",style:{color:"white"}})," Run Configuration"),this.state.showConfigButton?n.a.createElement(D.a,{onClick:this.saveResults,style:{marginLeft:"10px"}},n.a.createElement("i",{className:"fa fa-sticky-note",style:{color:"white"}})," ","Show results"):"",this.state.showLogButton?n.a.createElement("a",{target:"_blank",id:"downloadlink",ref:"file"},n.a.createElement(D.a,{onClick:this.showLogs,style:{marginLeft:"10px"}},n.a.createElement("i",{className:"fa fa-cog",style:{color:"white"}})," ","Logs")):"")))),n.a.createElement(F.a,{md:"3"},this.state.panelData.filter(e=>!0===e.showPanel).map(e=>n.a.createElement(y,{key:e.nodePath+e.numNodeType,panelData:e,updateParentPanelData:this.updateParentPanelData})))))}},layout:"/admin"}];var U=class extends n.a.Component{constructor(e){super(e),this.state={isOpen:!1,dropdownOpen:!1,color:"transparent"},this.toggle=this.toggle.bind(this),this.dropdownToggle=this.dropdownToggle.bind(this),this.sidebarToggle=n.a.createRef()}toggle(){this.state.isOpen?this.setState({color:"transparent"}):this.setState({color:"dark"}),this.setState({isOpen:!this.state.isOpen})}dropdownToggle(e){this.setState({dropdownOpen:!this.state.dropdownOpen})}getBrand(){var e="Default Brand";return A.map((t,a)=>(-1!==window.location.href.indexOf(t.layout+t.path)&&(e=t.name),null)),e}openSidebar(){document.documentElement.classList.toggle("nav-open"),this.sidebarToggle.current.classList.toggle("toggled")}updateColor(){window.innerWidth<993&&this.state.isOpen?this.setState({color:"dark"}):this.setState({color:"transparent"})}componentDidMount(){window.addEventListener("resize",this.updateColor.bind(this))}componentDidUpdate(e){window.innerWidth<993&&e.history.location.pathname!==e.location.pathname&&-1!==document.documentElement.className.indexOf("nav-open")&&(document.documentElement.classList.toggle("nav-open"),this.sidebarToggle.current.classList.toggle("toggled"))}render(){return n.a.createElement(c.a,{color:-1!==this.props.location.pathname.indexOf("full-screen-maps")?"dark":this.state.color,expand:"lg",className:-1!==this.props.location.pathname.indexOf("full-screen-maps")?"navbar-absolute fixed-top":"navbar-absolute fixed-top "+("transparent"===this.state.color?"navbar-transparent ":"")},n.a.createElement(p.a,{fluid:!0},n.a.createElement("div",{className:"navbar-wrapper"},n.a.createElement("div",{className:"navbar-toggle"}),n.a.createElement(h.a,{href:"/"},n.a.createElement("img",{className:"logo-height",alt:"...",src:a(90)}),n.a.createElement("span",{className:"logo-title"},"DEER Web UI")))))}};var X,Q=class extends n.a.Component{render(){return n.a.createElement("footer",{className:"footer"+(this.props.default?" footer-default":"")},n.a.createElement(p.a,{fluid:!!this.props.fluid},n.a.createElement(S.a,null,n.a.createElement("nav",{className:"footer-nav"},n.a.createElement("ul",null,n.a.createElement("li",null,n.a.createElement("a",{href:"https://www.creative-tim.com",target:"_blank"})),n.a.createElement("li",null,n.a.createElement("a",{href:"https://blog.creative-tim.com",target:"_blank"})),n.a.createElement("li",null,n.a.createElement("a",{href:"https://www.creative-tim.com/license",target:"_blank"})))),n.a.createElement("div",{className:"credits ml-auto"}))))}};a(91);var G=class extends n.a.Component{constructor(e){super(e),this.handleActiveClick=(e=>{this.setState({activeColor:e})}),this.handleBgClick=(e=>{this.setState({backgroundColor:e})}),this.state={backgroundColor:"black",activeColor:"info"},this.mainPanel=n.a.createRef()}componentDidMount(){navigator.platform.indexOf("Win")>-1&&(X=new d.a(this.mainPanel.current),document.body.classList.toggle("perfect-scrollbar-on"))}componentWillUnmount(){navigator.platform.indexOf("Win")>-1&&(X.destroy(),document.body.classList.toggle("perfect-scrollbar-on"))}componentDidUpdate(e){"PUSH"===e.history.action&&(this.mainPanel.current.scrollTop=0,document.scrollingElement.scrollTop=0)}render(){return n.a.createElement("div",{className:"wrapper"},n.a.createElement("div",{className:"main-panel",ref:this.mainPanel},n.a.createElement(U,this.props),n.a.createElement(o.d,null,A.map((e,t)=>n.a.createElement(o.b,{path:e.layout+e.path,component:e.component,key:t}))),n.a.createElement(Q,{fluid:!0})))}},H=Object(l.a)();i.a.render(n.a.createElement(o.c,{history:H},n.a.createElement(o.d,null,n.a.createElement(o.b,{path:"/admin",render:e=>n.a.createElement(G,e)}),n.a.createElement(o.a,{to:"/admin/dashboard"}))),document.getElementById("root"))}},[[53,1,2]]]);
//# sourceMappingURL=main.49de3bb6.chunk.js.map