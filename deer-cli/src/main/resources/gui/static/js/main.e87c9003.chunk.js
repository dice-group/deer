(window.webpackJsonp=window.webpackJsonp||[]).push([[0],{53:function(e,t,r){e.exports=r(95)},59:function(e,t,r){},60:function(e,t,r){},63:function(e,t,r){},65:function(e,t,r){},69:function(e,t){},71:function(e,t){},90:function(e,t,r){e.exports=r.p+"static/media/deer-logo.b55379b3.svg"},91:function(e,t,r){},95:function(e,t,r){"use strict";r.r(t);var a=r(1),s=r.n(a),o=r(14),i=r.n(o),n=r(10),p=r(115),l=(r(58),r(59),r(60),r(61),r(62),r(46)),d=r(112),c=r(113),m=r(114),h=(r(63),r(47)),u=r.n(h);r(65);var g=class extends s.a.Component{constructor(e){super(e);var t=this;this.linkWidget=this.addWidget("button","read more",this.linkName,function(e){e&&window.open(t.linkName,"_blank")}),this.widgets_start_y=70,this.widgets_up=!1,this.onDrawForeground=((e,t)=>{if(!this.flags.collapsed){e.font="12px Arial";var r=null;this.message[0]&&(r=this.message[0].replace(/"/g,"")),r=r||"Description of the node ...Continue description of the node ...";for(var a=this.splitStringForCanvas(r).split("\n"),s=0;s<a.length;s++)e.fillText(a[s],10,55+12*s)}})}splitStringForCanvas(e){var t="",r=27;this.props.includes("Operator")&&(r=49);for(var a=new RegExp(".{1,"+r+"}","g"),s=e.match(a),o=0;o<s.length;o++)t+=s[o].trim()+"\n";return t}render(){return s.a.createElement("div",null)}},f=r(116),w=r(97),P=r(98),E=r(99),b=r(100),y=r(101),v=r(102),x=r(103),O=r(104),C=r(105),k=r(106),N=r(107),S=r(108),F=r(117),j=r(109),R=r(110),q=r(111),Q=r(94),D=Q.DataFactory,I=D.namedNode,L=D.literal,M=D.defaultGraph,U=window.location.href,A=(window.location.hostname,window.location.port,window.LiteGraph);A.registered_node_types={},A.searchbox_extras={};var T=[{path:"/dashboard",name:"Dashboard",icon:"nc-icon nc-bank",component:class extends s.a.Component{constructor(e){super(e),this.getBaseUrl=(()=>new RegExp(/^.*\//).exec(window.location.href)),this.callbackFunction=(e=>{console.log(e),this.setState({formProperties:e})}),this.resizeWindowFunc=(()=>{var e=document.getElementById("mycanvas"),t=document.getElementById("parentCanvas");e.width=t.offsetWidth,e.height=t.offsetHeight,null!=this.state.graphCanvas&&this.state.graphCanvas.resize()}),this.getPropertyFirst=(e=>{e.forEach(e=>{var t=this.state.fullContent.filter(t=>t.subject.id.includes(e.object.id));if(t[0]&&!t[0].object.id.includes("_:n")){var r=this.state.tempXoneNodeParams;r.push(t[0].object.id),this.setState({tempXoneNodeParams:r})}!e.object.id.includes("nil")&&e.predicate.id.includes("rest")&&this.getPropertyFirst(t)})}),this.getPropertiesForNode=(e=>{this.setState({tempXoneNodeParams:[]});var t=this.state.fullContent.filter(t=>t.subject.id.includes(e)&&t.predicate.id.includes("property")).map(e=>e.object.id),r=this.state.fullContent.filter(t=>t.subject.id.includes(e)&&t.predicate.id.includes("xone")),a=[];return r.forEach(e=>{var t=this.state.fullContent.filter(t=>t.subject.id.includes(e.object.id));this.getPropertyFirst(t),a=this.state.tempXoneNodeParams}),{basicProps:t,xone:a}}),this.getPropertyName=(e=>{var t=e.split("_");return t.length>2?t[t.length-1]:t[1]}),this.getInputPorts=(e=>this.state.fullContent.filter(t=>t.subject.id.includes(e)&&t.predicate.id.includes("maxInPorts")).map(e=>e.object.id)),this.getOutputPorts=(e=>this.state.fullContent.filter(t=>t.subject.id.includes(e)&&t.predicate.id.includes("maxOutPorts")).map(e=>e.object.id)),this.getMessage=(e=>this.state.fullContent.filter(t=>t.subject.id.includes(e)&&t.predicate.id.includes("comment")).map(e=>e.object.id)),this.getUrlForTheNode=(e=>this.state.fullContent.filter(t=>t.subject.id.includes(e)&&t.predicate.id.includes("seeAlso")).map(e=>e.object.id)),this.initializeNode=(e=>{var t=this.getPropertiesForNode(e),r=this.getInputPorts(e)[0],a=this.getOutputPorts(e)[0],s=r.match(/\d+/)[0],o=a.match(/\d+/)[0],i=this.getMessage(e),n=this.getUrlForTheNode(e),p={name:"some text"},l=t.basicProps.map(e=>this.getPropertyName(e));if(l.forEach(e=>{p[e]="some text"}),(l=t.xone.map(e=>this.getPropertyName(e))).forEach(e=>{p[e]="some text"}),e.includes("Operator")){class t extends g{constructor(e){super(e);for(var t=0;t<s;t++)this.addInput("input","text");for(var r=0;r<o;r++)this.addOutput("output","text");this.properties=Object.create(p),this.message=i,this.linkName=n}}t.title=e,t.size=[320,100],t.color="#664d00",t.bgcolor="#8c6a00",A.registerNodeType("Operator/"+e,t)}else if(e.includes("Reader")){class t extends g{constructor(e){super(e),this.addOutput("output","text"),this.properties=Object.create(p),this.message=i,this.linkName=n}}t.title=e,t.size=[180,100],t.color="#223322",t.bgcolor="#335533",A.registerNodeType("Reader/"+e,t)}else{class t extends g{constructor(e){super(e),this.addInput("input","text"),this.properties=Object.create(p),this.message=i,this.linkName=n}}t.title=e,t.size=[180,100],t.color="#223322",t.bgcolor="#335533",A.registerNodeType("Writer/"+e,t)}}),this.showNode=(e=>{if(e.predicate.id.includes("targetClass")){var t=e.object.id.split("http://w3id.org/deer/")[1],r=this.state.nodesArray;t&&(r.add(t),this.initializeNode(t)),this.setState({nodesArray:r})}}),this.toggleDropdown=(()=>{this.setState({show:!this.state.show})}),this.getInputLink=(e=>{for(var t=0;t<e.inputs.length;t++){console.log("i = "+t);var r=e.inputs[t].link;console.log(r);var a=this.state.graph.links[r];if(a){var s=this.state.graph.getNodeById(a.origin_id),o=this.state.inputPorts;o.push(s),this.setState({inputPorts:o})}}}),this.saveConfig=(()=>{var e=this.state.graph.serialize(),t=(D.quad(I("https://ruben.verborgh.org/profile/#me"),I("http://xmlns.com/foaf/0.1/givenName"),L("Ruben","en"),M()),new Q.Writer({prefixes:this.state.prefixes},{format:"N-Triples"}));new Q.Parser({format:"N3",blankNodePrefix:""}),e.nodes.map((e,r)=>{if(t.addQuad(I("urn:example:demo/"+e.properties.name),I("http://www.w3.org/1999/02/22-rdf-syntax-ns#type"),I("http://w3id.org/deer/"+e.type.split("/")[1])),e.inputs){for(var a=0;a<e.inputs.length;a++)if(e.inputs[a].link){var s=e.inputs[a].link;console.log(s);var o=this.state.graph.links[s];if(o){var i=this.state.graph.getNodeById(o.origin_id),n=this.state.inputPorts;n.push(i),this.setState({inputPorts:n,inputLinkId:s})}}console.log(this.state.inputPorts);var p=this.state.inputPorts.map((e,r)=>t.blank([{predicate:I("http://w3id.org/fcage/fromNode"),object:I("urn:example:demo/"+e.properties.name)},{predicate:I("http://w3id.org/fcage/fromPort"),object:L(r)}]));t.addQuad(I("urn:example:demo/"+e.properties.name),I("http://w3id.org/fcage/hasInput"),t.list(p)),this.setState({inputPorts:[],inputLinkId:""})}"Reader/FileModelReader"===e.type&&(this.state.formProperties&&"Reader/FileModelReader"===this.state.formProperties.node.type&&(e.properties.name=this.state.properties.name,e.properties.fromUri=this.state.formProperties.fromUri,e.properties.fromPath=this.state.formProperties.fromPath),e.properties.fromUri&&t.addQuad(I("urn:example:demo/"+e.properties.name),I("http://w3id.org/deer/fromUri"),L(e.properties.fromUri)),e.properties.fromPath&&t.addQuad(I("urn:example:demo/"+e.properties.name),I("http://w3id.org/deer/fromPath"),L(e.properties.fromPath))),"Reader/SparqlModelReader"===e.type&&(this.state.formProperties&&"Reader/SparqlModelReader"===this.state.formProperties.node.type&&(e.properties.name=this.state.formProperties.name,e.properties.fromEndpoint=this.state.formProperties.fromEndpoint,e.properties.sparqlDescribeOf=this.state.formProperties.useSparqlDescribeOf,e.properties.useSparqlConstruct=this.state.formProperties.useSparqlConstruct),e.properties.fromEndpoint&&t.addQuad(I("urn:example:demo/"+e.properties.name),I("http://w3id.org/deer/fromEndpoint"),I(e.properties.fromEndpoint)),e.properties.sparqlDescribeOf&&t.addQuad(I("urn:example:demo/"+e.properties.name),I("http://w3id.org/deer/useSparqlDescribeOf"),I(e.properties.sparqlDescribeOf)),e.properties.useSparqlConstruct&&t.addQuad(I("urn:example:demo/"+e.properties.name),I("http://w3id.org/deer/useSparqlConstruct"),L(e.properties.useSparqlConstruct))),"Writer/FileModelWriter"===e.type&&(this.state.formProperties&&"Writer/FileModelWriter"===this.state.formProperties.node.type&&(e.properties.name=this.state.formProperties.name,e.properties.outputFile=this.state.formProperties.outputFile,e.properties.outputFormat=this.state.formProperties.outputFormat),e.properties.outputFile&&e.properties.outputFormat&&(t.addQuad(I("urn:example:demo/"+e.properties.name),I("http://w3id.org/deer/outputFile"),L(e.properties.outputFile)),t.addQuad(I("urn:example:demo/"+e.properties.name),I("http://w3id.org/deer/outputFormat"),L(e.properties.outputFormat)))),"Operator/FilterEnrichmentOperator"===e.type&&(this.state.formProperties&&"Operator/FilterEnrichmentOperator"===this.state.formProperties.node.type&&(e.properties.name=this.state.formProperties.name,e.properties.selector=this.state.formProperties.selector,e.properties.resource=this.state.formProperties.resource,e.properties.sparqlConstructQuery=this.state.formProperties.sparqlConstructQuery),e.properties.selector?t.addQuad(I("urn:example:demo/"+e.properties.name),I("http://w3id.org/deer/selector"),t.blank([{predicate:I("http://w3id.org/deer/"+e.properties.selector),object:I(e.properties.resource)}])):e.properties.sparqlConstructQuery&&t.addQuad(I("urn:example:demo/"+e.properties.name),I("http://w3id.org/deer/sparqlConstructQuery"),L(e.properties.sparqlConstructQuery))),"Operator/DereferencingEnrichmentOperator"===e.type&&(this.state.formProperties&&"Operator/DereferencingEnrichmentOperator"===this.state.formProperties.node.type&&(e.properties.name=this.state.properties.name,e.properties.lookUpPrefix=this.state.formProperties.lookUpPrefix,e.properties.dereferencingProperty=this.state.formProperties.dereferencingProperty,e.properties.importProperty=this.state.formProperties.importProperty),t.addQuad(I("urn:example:demo/"+e.properties.name),I("http://w3id.org/deer/operation"),t.blank([{predicate:I("urn:example:demo/lookUpPrefix"),object:L(e.properties.lookUpPrefix)},{predicate:I("urn:example:demo/dereferencingProperty"),object:L(e.properties.dereferencingProperty)},{predicate:I("urn:example:demo/importProperty"),object:L(e.properties.importProperty)}]))),"Operator/GeoFusionEnrichmentOperator"===e.type&&(this.state.formProperties&&"Operator/GeoFusionEnrichmentOperator"===this.state.formProperties.node.type&&(e.properties.name=this.state.properties.name,e.properties.fusionAction=this.state.formProperties.fusionAction,e.properties.mergeOtherStatements=this.state.formProperties.mergeOtherStatements),t.addQuad(I("urn:example:demo/"+e.properties.name),I("http://w3id.org/deer/fusionAction"),L(e.properties.fusionAction)),t.addQuad(I("urn:example:demo/"+e.properties.name),I("http://w3id.org/deer/mergeOtherStatements"),L(e.properties.mergeOtherStatements))),"Operator/AuthorityConformationEnrichmentOperator"===e.type&&(this.state.formProperties&&"Operator/AuthorityConformationEnrichmentOperator"===this.state.formProperties.node.type&&(e.properties.name=this.state.properties.name,e.properties.sourceSubjectAuthority=this.state.formProperties.sourceSubjectAuthority,e.properties.targetSubjectAuthority=this.state.formProperties.targetSubjectAuthority),t.addQuad(I("urn:example:demo/"+e.properties.name),I("http://w3id.org/deer/operation"),t.blank([{predicate:I("http://w3id.org/deer/sourceSubjectAuthority"),object:I(e.properties.sourceSubjectAuthority)},{predicate:I("http://w3id.org/deer/targetSubjectAuthority"),object:I(e.properties.targetSubjectAuthority)}]))),"Operator/PredicateConformationEnrichmentOperator"===e.type&&(this.state.formProperties&&"Operator/PredicateConformationEnrichmentOperator"===this.state.formProperties.node.type&&(e.properties.name=this.state.properties.name,e.properties.sourcePredicate=this.state.formProperties.sourcePredicate,e.properties.targetPredicate=this.state.formProperties.targetPredicate),t.addQuad(I("urn:example:demo/"+e.properties.name),I("http://w3id.org/deer/operation"),t.blank([{predicate:I("http://w3id.org/deer/sourcePredicate"),object:L(e.properties.sourcePredicate)},{predicate:I("http://w3id.org/deer/targetPredicate"),object:L(e.properties.targetPredicate)}]))),"Operator/GeoDistanceEnrichmentOperator"===e.type&&(this.state.formProperties&&"Operator/GeoDistanceEnrichmentOperator"===this.state.formProperties.node.type&&(e.properties.name=this.state.properties.name,e.properties.selectPredicate=this.state.formProperties.selectPredicate,e.properties.distancePredicate=this.state.formProperties.distancePredicate),t.addQuad(I("urn:example:demo/"+e.properties.name),I("http://w3id.org/deer/selectPredicate"),I(e.properties.selectPredicate)),t.addQuad(I("urn:example:demo/"+e.properties.name),I("http://w3id.org/deer/distancePredicate"),I(e.properties.distancePredicate))),"Operator/LinkingEnrichmentOperator"===e.type&&(this.state.formProperties&&"Operator/LinkingEnrichmentOperator"===this.state.formProperties.node.type&&(e.properties.name=this.state.formProperties.name,e.properties.specFile=this.state.formProperties.specFile,e.properties.linksPart=this.state.formProperties.linksPart,e.properties.selectMode=this.state.formProperties.selectMode,e.properties.linkSpecification=this.state.formProperties.linkSpecification,e.properties.linkingPredicate=this.state.formProperties.linkingPredicate,e.properties.threshold=this.state.formProperties.threshold),t.addQuad(I("urn:example:demo/"+e.properties.name),I("http://w3id.org/deer/specFile"),L(e.properties.specFile)),t.addQuad(I("urn:example:demo/"+e.properties.name),I("http://w3id.org/deer/linksPart"),L(e.properties.linksPart)),t.addQuad(I("urn:example:demo/"+e.properties.name),I("http://w3id.org/deer/selectMode"),L(e.properties.selectMode)),e.properties.linkSpecification&&t.addQuad(I("urn:example:demo/"+e.properties.name),I("http://w3id.org/deer/linkSpecification"),L(e.properties.linkSpecification)),e.properties.linkingPredicate&&t.addQuad(I("urn:example:demo/"+e.properties.name),I("http://w3id.org/deer/linkingPredicate"),L(e.properties.linkingPredicate)),e.properties.threshold&&t.addQuad(I("urn:example:demo/"+e.properties.name),I("http://w3id.org/deer/threshold"),L(e.properties.threshold))),"Operator/NEREnrichmentOperator"===e.type&&(this.state.formProperties&&"Operator/NEREnrichmentOperator"===this.state.formProperties.node.type&&(e.properties.name=this.state.properties.name,e.properties.literalProperty=this.state.formProperties.literalProperty,e.properties.importProperty=this.state.formProperties.importProperty,e.properties.neType=this.state.formProperties.neType,e.properties.foxUrl=this.state.formProperties.foxUrl),t.addQuad(I("urn:example:demo/"+e.properties.name),I("http://w3id.org/deer/literalProperty"),I(e.properties.literalProperty)),t.addQuad(I("urn:example:demo/"+e.properties.name),I("http://w3id.org/deer/importProperty"),I(e.properties.distancePredimportPropertyicate)),t.addQuad(I("urn:example:demo/"+e.properties.name),I("http://w3id.org/deer/foxUrl"),I(e.properties.foxUrl)),t.addQuad(I("urn:example:demo/"+e.properties.name),I("http://w3id.org/deer/neType"),L(e.properties.neType))),"Operator/SparqlUpdateEnrichmentOperator"===e.type&&(this.state.formProperties&&"Operator/NEREnrichmentOperator"===this.state.formProperties.node.type&&(e.properties.name=this.state.properties.name,e.properties.sparqlUpdateQuery=this.state.formProperties.sparqlUpdateQuery),t.addQuad(I("urn:example:demo/"+e.properties.name),I("http://w3id.org/deer/sparqlUpdateQuery"),L(e.properties.sparqlUpdateQuery)))}),t.end((e,t)=>{console.log(t),this.submitConfig(t),t=""})}),this.downloadResults=(e=>{fetch(U+"/result/"+this.state.requestID+"/"+this.state.availableFiles[e]).then(t=>{t.blob().then(t=>{var r=window.URL.createObjectURL(t),a=document.createElement("a");a.href=r,a.download=this.state.availableFiles[e],a.click()})})}),this.submitConfig=(e=>{var t=new Blob([e],{type:"text/ttl"}),r=new File([t],"config.ttl"),a=new FormData;a.append("config",r),fetch(U+"/submit",{method:"POST",body:a}).then(e=>e.json()).then(e=>{e.error&&-1===e.error.code?this.setState({visible:!0}):e.requestId&&(this.setState({requestID:e.requestId,showLogButton:!0}),this.interval=setInterval(this.getStatusForRequest,1e3))})}),this.getStatusForRequest=(()=>{fetch(U+"/status/"+this.state.requestID).then(function(e){return e.json()}).then(e=>{2===e.status.code&&(clearInterval(this.interval),this.setState({requestCompleteModal:!0,showConfigButton:!0}),this.getResults())})}),this.getResults=(()=>{fetch(U+"/results/"+this.state.requestID).then(function(e){return e.json()}).then(e=>{console.log(e.availableFiles),this.setState({availableFiles:e.availableFiles})})}),this.showLogs=(()=>{fetch(U+"/logs/"+this.state.requestID).then(function(e){var t=document.getElementById("downloadlink");t.href=e.url,t.click()})}),this.toggle=(()=>{this.setState({visible:!this.state.visible})}),this.getFilteredOptions=(e=>{this.setState({afterFilteredSuggestions:this.state.prefixOptions.filter(t=>t.toLowerCase().includes(e.target.value.toLowerCase())),userInput:e.target.value})}),this.selectedPrefix=(e=>{this.setState({afterFilteredSuggestions:[],namespace:this.state.contexts[e.target.value],userInput:e.target.value,isDisabled:!1})}),this.handleNamespaceChange=(e=>{this.setState({namespace:e.target.value,isDisabled:!1})}),this.addNewPrefixes=(e=>{this.setState({addNewPrefixes:this.state.addNewPrefixes.concat(this.state.userInput)}),this.state.prefixes[this.state.userInput]=this.state.namespace}),this.saveResults=(()=>{this.setState({showResultModal:!this.state.showResultModal})}),this.toggleResultModal=(()=>{this.setState({showResultModal:!this.state.showResultModal})}),this.toggleRequestCompleteModal=(()=>{this.setState({requestCompleteModal:!this.state.requestCompleteModal})}),this.uploadFiles=(()=>{}),this.state={graph:new A.LGraph,graphCanvas:null,outputLinks:[],prefixOptions:[],config:"",isModalOpen:!1,namespace:"",userInput:"",afterFilteredSuggestions:[],addNewPrefixes:[],showForm:!1,showComponent:"",node:"",isDisabled:!1,nodeArr:[],file:"",visible:!1,requestID:"",showResultModal:!1,requestCompleteModal:!1,showConfigButton:!1,availableFiles:[],showLogButton:!1,formProperties:"",input1:"",input2:"",prefixes:{example:"urn:example:demo/",foaf:"http://xmlns.com/foaf/0.1/",dbpedia:"http://dbpedia.org/resource/",deer:"http://w3id.org/deer/",fcage:"http://w3id.org/fcage/",geo:"http://www.w3.org/2003/01/geo/wgs84_pos#",owl:"http://www.w3.org/2002/07/owl#",rdf:"http://www.w3.org/1999/02/22-rdf-syntax-ns#",rdfs:"http://www.w3.org/2000/01/rdf-schema#",xsd:"http://www.w3.org/2001/XMLSchema#"},nodesArray:new Set,quads:[],tempXoneNodeParams:[],fullContent:null,inputPorts:[],inputLinkId:""}}componentWillUnmount(){window.removeEventListener("resize",this.resizeWindowFunc)}componentDidMount(){this.resizeWindowFunc(),window.addEventListener("resize",this.resizeWindowFunc);var e=new A.LGraphCanvas("#mycanvas",this.state.graph);this.setState({graphCanvas:e}),this.state.graph.start(),e.show_info=!1,fetch("https://prefix.cc/context").then(function(e){return e.json()}).then(e=>{this.setState({contexts:e["@context"],prefixOptions:Object.keys(e["@context"])})}),""!==this.state.userInput&&""!==this.state.namespace||this.setState({isDisabled:!0});var t=new Q.Parser;fetch(U+"/shapes").then(function(e){return e.text()}).then(e=>{var r=(new Q.Parser).parse(e);this.setState({fullContent:r}),t.parse(e,(e,t,r)=>{t&&this.showNode(t)})})}render(){return u.a.map(this.state.prefixOptions,(e,t)=>({key:e,text:e,value:e})),s.a.createElement("div",{className:"content"},s.a.createElement(f.a,{isOpen:this.state.visible,toggle:this.toggle},s.a.createElement(w.a,{toggle:this.toggle},"Incorrect Configuration"),s.a.createElement(P.a,null,"Please input all the required fields and connections.")),s.a.createElement(f.a,{isOpen:this.state.requestCompleteModal,toggle:this.toggleRequestCompleteModal},s.a.createElement(w.a,{toggle:this.toggleRequestCompleteModal},"Status"),s.a.createElement(P.a,null,"The results are ready. You can download them by clicking 'Show Results'.")),s.a.createElement(f.a,{isOpen:this.state.showResultModal,toggle:this.toggleResultModal},s.a.createElement(w.a,{toggle:this.toggleResultModal},"Results"),s.a.createElement(P.a,null,s.a.createElement(E.a,null,s.a.createElement("thead",null,s.a.createElement("tr",null,s.a.createElement("th",null,"File"),s.a.createElement("th",null,"Download"))),s.a.createElement("tbody",null,this.state.availableFiles.map((e,t)=>s.a.createElement("tr",{key:t},s.a.createElement("td",null,e),s.a.createElement("td",null,s.a.createElement(b.a,{onClick:()=>this.downloadResults(t)},s.a.createElement("i",{className:"fa fa-download",style:{color:"white"}})," ","Download")))))))),s.a.createElement(y.a,null,s.a.createElement(v.a,{lg:"9",md:"9",sm:"9"},s.a.createElement(x.a,{className:"card-stats"},s.a.createElement("div",{className:"numbers"},s.a.createElement(O.a,{tag:"p"},"Prefixes")),s.a.createElement(C.a,null,s.a.createElement(y.a,null,s.a.createElement(v.a,{md:"12",xs:"12"},s.a.createElement(k.a,null,s.a.createElement(y.a,null,s.a.createElement(v.a,{md:"4"},s.a.createElement(a.Fragment,null,s.a.createElement(N.a,{className:"dropdown"},s.a.createElement("label",null,"Label"),s.a.createElement(S.a,{placeholder:"Label",type:"text",value:this.state.userInput,onChange:this.getFilteredOptions}),s.a.createElement(F.a,{className:"dropdown-content",toggle:this.toggleDropdown},this.state.afterFilteredSuggestions.map((e,t)=>s.a.createElement(j.a,{className:"dropdown-item",value:e,onClick:this.selectedPrefix},e)))))),s.a.createElement(v.a,{md:"4"},s.a.createElement(N.a,null,s.a.createElement("label",null,"Namespace"),s.a.createElement(S.a,{placeholder:"Namespace",type:"text",value:this.state.namespace,onChange:this.handleNamespaceChange}))),s.a.createElement(v.a,{md:"4"},s.a.createElement(b.a,{disabled:this.state.isDisabled,className:"btn-round prefixBtn",color:"primary",onClick:this.addNewPrefixes},"Add prefix"))))))),s.a.createElement(R.a,null,s.a.createElement("hr",null),s.a.createElement("div",{className:"stats"},this.state.addNewPrefixes.map(e=>s.a.createElement(q.a,{variant:"light"}," ",e)))))),s.a.createElement(v.a,{lg:"3",md:"3",sm:"3"},s.a.createElement(x.a,{className:"card-stats"},s.a.createElement("div",{className:"numbers"},s.a.createElement(O.a,{tag:"p"},"Upload Files")),s.a.createElement(C.a,null,s.a.createElement(y.a,null,s.a.createElement(v.a,{md:"8"}," ",s.a.createElement("input",{id:"input-b2",name:"input-b2",type:"file",className:"file inputFile","data-show-preview":"false"})),s.a.createElement(v.a,{md:"3"}," ",s.a.createElement(b.a,{className:"btn-round uploadBtn",color:"primary",onClick:this.uploadFiles},"Upload")))),s.a.createElement("hr",null),s.a.createElement(R.a,null)))),s.a.createElement(y.a,null,s.a.createElement(v.a,{md:"9"},s.a.createElement(x.a,null,s.a.createElement("div",{className:"numbers"},s.a.createElement(O.a,{tag:"p"},"Graph")),s.a.createElement(C.a,null,s.a.createElement("div",{id:"parentCanvas",className:"litegraph litegraph-editor"},s.a.createElement("canvas",{id:"mycanvas",height:"600",width:"1000"})," ")),s.a.createElement(R.a,null,s.a.createElement("hr",null),s.a.createElement("div",{className:"stats"},s.a.createElement(b.a,{onClick:this.saveConfig},s.a.createElement("i",{className:"fa fa-cog",style:{color:"white"}})," Run Configuration"),this.state.showConfigButton?s.a.createElement(b.a,{onClick:this.saveResults,style:{marginLeft:"10px"}},s.a.createElement("i",{className:"fa fa-sticky-note",style:{color:"white"}})," ","Show results"):"",this.state.showLogButton?s.a.createElement("a",{target:"_blank",id:"downloadlink",ref:"file"},s.a.createElement(b.a,{onClick:this.showLogs,style:{marginLeft:"10px"}},s.a.createElement("i",{className:"fa fa-cog",style:{color:"white"}})," ","Logs")):""))))))}},layout:"/admin"}];var W=class extends s.a.Component{constructor(e){super(e),this.state={isOpen:!1,dropdownOpen:!1,color:"transparent"},this.toggle=this.toggle.bind(this),this.dropdownToggle=this.dropdownToggle.bind(this),this.sidebarToggle=s.a.createRef()}toggle(){this.state.isOpen?this.setState({color:"transparent"}):this.setState({color:"dark"}),this.setState({isOpen:!this.state.isOpen})}dropdownToggle(e){this.setState({dropdownOpen:!this.state.dropdownOpen})}getBrand(){var e="Default Brand";return T.map((t,r)=>(-1!==window.location.href.indexOf(t.layout+t.path)&&(e=t.name),null)),e}openSidebar(){document.documentElement.classList.toggle("nav-open"),this.sidebarToggle.current.classList.toggle("toggled")}updateColor(){window.innerWidth<993&&this.state.isOpen?this.setState({color:"dark"}):this.setState({color:"transparent"})}componentDidMount(){window.addEventListener("resize",this.updateColor.bind(this))}componentDidUpdate(e){window.innerWidth<993&&e.history.location.pathname!==e.location.pathname&&-1!==document.documentElement.className.indexOf("nav-open")&&(document.documentElement.classList.toggle("nav-open"),this.sidebarToggle.current.classList.toggle("toggled"))}render(){return s.a.createElement(d.a,{color:-1!==this.props.location.pathname.indexOf("full-screen-maps")?"dark":this.state.color,expand:"lg",className:-1!==this.props.location.pathname.indexOf("full-screen-maps")?"navbar-absolute fixed-top":"navbar-absolute fixed-top "+("transparent"===this.state.color?"navbar-transparent ":"")},s.a.createElement(c.a,{fluid:!0},s.a.createElement("div",{className:"navbar-wrapper"},s.a.createElement("div",{className:"navbar-toggle"}),s.a.createElement(m.a,{href:"/"},s.a.createElement("img",{className:"logo-height",alt:"...",src:r(90)}),s.a.createElement("span",{className:"logo-title"},"DEER Web UI")))))}};var B,_=class extends s.a.Component{render(){return s.a.createElement("footer",{className:"footer"+(this.props.default?" footer-default":"")},s.a.createElement(c.a,{fluid:!!this.props.fluid},s.a.createElement(y.a,null,s.a.createElement("nav",{className:"footer-nav"},s.a.createElement("ul",null,s.a.createElement("li",null,s.a.createElement("a",{href:"https://www.creative-tim.com",target:"_blank"})),s.a.createElement("li",null,s.a.createElement("a",{href:"https://blog.creative-tim.com",target:"_blank"})),s.a.createElement("li",null,s.a.createElement("a",{href:"https://www.creative-tim.com/license",target:"_blank"})))),s.a.createElement("div",{className:"credits ml-auto"}))))}};r(91);var z=class extends s.a.Component{constructor(e){super(e),this.handleActiveClick=(e=>{this.setState({activeColor:e})}),this.handleBgClick=(e=>{this.setState({backgroundColor:e})}),this.state={backgroundColor:"black",activeColor:"info"},this.mainPanel=s.a.createRef()}componentDidMount(){navigator.platform.indexOf("Win")>-1&&(B=new l.a(this.mainPanel.current),document.body.classList.toggle("perfect-scrollbar-on"))}componentWillUnmount(){navigator.platform.indexOf("Win")>-1&&(B.destroy(),document.body.classList.toggle("perfect-scrollbar-on"))}componentDidUpdate(e){"PUSH"===e.history.action&&(this.mainPanel.current.scrollTop=0,document.scrollingElement.scrollTop=0)}render(){return s.a.createElement("div",{className:"wrapper"},s.a.createElement("div",{className:"main-panel",ref:this.mainPanel},s.a.createElement(W,this.props),s.a.createElement(p.d,null,T.map((e,t)=>s.a.createElement(p.b,{path:e.layout+e.path,component:e.component,key:t}))),s.a.createElement(_,{fluid:!0})))}},G=Object(n.a)();i.a.render(s.a.createElement(p.c,{history:G},s.a.createElement(p.d,null,s.a.createElement(p.b,{path:"/admin",render:e=>s.a.createElement(z,e)}),s.a.createElement(p.a,{to:"/admin/dashboard"}))),document.getElementById("root"))}},[[53,1,2]]]);
//# sourceMappingURL=main.e87c9003.chunk.js.map