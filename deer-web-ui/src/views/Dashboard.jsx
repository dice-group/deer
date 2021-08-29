/*!

=========================================================
* Paper Dashboard React - v1.1.0
=========================================================

* Product Page: https://www.creative-tim.com/product/paper-dashboard-react
* Copyright 2019 Creative Tim (https://www.creative-tim.com)

* Licensed under MIT (https://github.com/creativetimofficial/paper-dashboard-react/blob/master/LICENSE.md)

* Coded by Creative Tim

=========================================================

* The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

*/
import React, { Fragment } from "react";
// react plugin used to create charts
import _ from "lodash";
import "./Dashboard.css";
import FactoryNode from "../components/FactoryNode";
import Panel from "../components/Panel";

// reactstrap components
import {
  Card,
  CardHeader,
  CardBody,
  CardFooter,
  CardTitle,
  Row,
  Col,
  Button,
  ModalHeader,
  Modal,
  ModalBody,
  Form,
  FormGroup,
  Input,
  Dropdown,
  DropdownItem,
  Badge,
  Table,
} from "reactstrap";

const N3 = require("n3");
const { DataFactory } = N3;
const { namedNode, literal, defaultGraph } = DataFactory;
const URI = window.location.href;
//const URI = "http://localhost:8080";
const BASE_URI = window.location.hostname + ":" + window.location.port;

const litegraph = window.LiteGraph;

class Dashboard extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      graph: new litegraph.LGraph(),
      graphCanvas: null,
      outputLinks: [],
      prefixOptions: [],
      config: ``,
      isModalOpen: false,
      namespace: "",
      userInput: "",
      afterFilteredSuggestions: [],
      addNewPrefixes: [],
      showForm: false,
      showComponent: "",
      node: "",
      isDisabled: false,
      nodeArr: [],
      file: "",
      visible: false,
      requestID: "",
      showResultModal: false,
      requestCompleteModal: false,
      showConfigButton: false,
      availableFiles: [],
      showLogButton: false,
      formProperties: "",
      input1: "",
      input2: "",
      prefixes: {
        example: "urn:example:demo/",
        foaf: "http://xmlns.com/foaf/0.1/",
        dbpedia: "http://dbpedia.org/resource/",
        deer: "https://w3id.org/deer/",
        fcage: "https://w3id.org/fcage/",
        geo: "http://www.w3.org/2003/01/geo/wgs84_pos#",
        owl: "http://www.w3.org/2002/07/owl#",
        rdf: "http://www.w3.org/1999/02/22-rdf-syntax-ns#",
        rdfs: "http://www.w3.org/2000/01/rdf-schema#",
        xsd: "http://www.w3.org/2001/XMLSchema#",
      },
      nodesArray: new Set(),
      quads: [],
      tempXoneNodeParams: [],
      fullContent: null,
      inputPorts: [],
      inputLinkId: '',
      selectedFiles: [],
      panelData: [
        // {
        //   numNodeType: 0,
        //   nodePath: "one/two",
        //   properties: ["a", "b"],
        //   showPanel: "false",
        // }
      ],
    };
  }

  updateParentPanelData = (temp, numNodeType) => {
    var curPanelData = Object.assign([], this.state.panelData);
    let id = curPanelData.findIndex(i => numNodeType === i.numNodeType);
    curPanelData[id] = temp;
    this.setState({
      panelData: curPanelData
    })
  };

  getBaseUrl = () => {
    var re = new RegExp(/^.*\//);
    return re.exec(window.location.href);
  };

  callbackFunction = (properties) => {
    // console.log(properties);
    this.setState({
      formProperties: properties,
    });
  };

  componentWillUnmount() {
    window.removeEventListener("resize", this.resizeWindowFunc);
  };

  resizeWindowFunc = () => {
    let canvas = document.getElementById("mycanvas");
    let parent = document.getElementById("parentCanvas");
    canvas.width = parent.offsetWidth;
    canvas.height = parent.offsetHeight;

    if (this.state.graphCanvas != null) {
      this.state.graphCanvas.resize();
    }

  };

  componentDidMount() {
    this.resizeWindowFunc();
    window.addEventListener("resize", this.resizeWindowFunc);

    var graphCanvas = new litegraph.LGraphCanvas("#mycanvas", this.state.graph);
    this.setState({
        graphCanvas: graphCanvas,
    });
    this.state.graph.start();
   
    graphCanvas.show_info = false;

    //returns the prefixes
    fetch("http://prefix.cc/context")
      .then(function (response) {
        return response.json();
      })
      .then((content) => {
        this.setState({
          contexts: content["@context"],
          prefixOptions: Object.keys(content["@context"]),
        });
      });

    if (this.state.userInput === "" || this.state.namespace === "") {
      this.setState({
        isDisabled: true,
      });
    }

    const parser = new N3.Parser();
    fetch(URI + "/shapes")
      .then(function (response) {
        return response.text();
      })
      .then((content) => {
        // console.log(content);
        const p= new N3.Parser();
        let fullContent = p.parse(content);
        this.setState({
          fullContent: fullContent,
        });

        parser.parse(content, (error, quad, prefixes) => {
          if(quad){
            //console.log(quad);
            this.showNode(quad);
          }
          else{

          }
          //  console.log("No node returned.");
        });
        
      });
  }

  getPropertyFirst = (quadsWithXone) => {
    quadsWithXone.forEach(i => {
        let quadI = this.state.fullContent.filter(quad => quad.subject.id.includes(i.object.id));
        if(quadI[0] && !quadI[0].object.id.includes("_:n")){
          let tempXoneNodeParams = this.state.tempXoneNodeParams;
          tempXoneNodeParams.push(quadI[0].object.id);
          this.setState({
            tempXoneNodeParams: tempXoneNodeParams,
          });
        }
        if(!i.object.id.includes('nil') && i.predicate.id.includes('rest')){
          this.getPropertyFirst(quadI);
        }
    });
  }

  getPropertiesForNode = (node) => {
    let arr = [];

    this.setState({
      tempXoneNodeParams: [],
    });

    let propsWithPropPredicate = this.state.fullContent.filter(quad => quad.subject.id.includes(node) && quad.predicate.id.includes("property")).map(i => i.object.id);
    let propsUnderSelectorNode = this.state.fullContent.filter(quad => quad.subject.id.includes(node) && quad.predicate.id.includes("property") && quad.object.id.split("_").length-1 == 2).map(i => i.object.id);
    let selectorNodeMinCount = this.state.fullContent.filter(quad => quad.subject.id.includes(node) && (quad.subject.id.includes("selector") || quad.subject.id.includes("operation")) && quad.predicate.id.includes("minCount")).map(i => i.object.id);
    let minCount = -1;
    if(selectorNodeMinCount.length){
      minCount = selectorNodeMinCount[0].split("^^")[0].match(/\d+/g)[0];
      console.log(node, minCount);
    }

    let propsUnderSelectorNodeWithMaxCount = [];
    propsUnderSelectorNode.forEach(nodeName => {
      for(let c = 0; c < minCount; c++){
        let num = this.state.fullContent.filter(quad => quad.subject.id.includes(nodeName) && quad.predicate.id.includes("maxCount")).map(i => i.object.id);
        let maxCount = num[0].split("^^")[0].match(/\d+/g)[0];
        let propName = this.getPropertyName(nodeName);
        if(c === 0){
          propsUnderSelectorNodeWithMaxCount.push({nodeSelectorProp: propName, maxCount: maxCount}); 
        } else {
          propsUnderSelectorNodeWithMaxCount.push({nodeSelectorProp: propName+c, maxCount: maxCount}); 
        }
      }
    });
    let propsWithXonePredicate = this.state.fullContent.filter(quad => quad.subject.id.includes(node) && quad.predicate.id.includes("xone")); 
    
    let propsFromXone = [];
    propsWithXonePredicate.forEach(pr => {
      let quadsWithXone = this.state.fullContent.filter(quad => quad.subject.id.includes(pr.object.id));
      this.getPropertyFirst(quadsWithXone);
      propsFromXone = this.state.tempXoneNodeParams;
    });

    let allNodeProps = {'basicProps': propsWithPropPredicate, 'xone': propsFromXone, 'propsSelector': {selectorMinCount: minCount, propsMaxCount: propsUnderSelectorNodeWithMaxCount}};
    return allNodeProps;
  }

  getPropertyName = (prop) => {
    let betweenDashesArray = prop.split('_');
    let property = "";
    if(betweenDashesArray.length > 2){
      property = betweenDashesArray[betweenDashesArray.length-1];
    } else {
      property = betweenDashesArray[1];
    }
    return property;
  }

  getInputPorts = (node) => {
    let inputPorts = this.state.fullContent.filter(quad => quad.subject.id.includes(node) && quad.predicate.id.includes("maxInPorts")).map(i => i.object.id);
    return inputPorts;
  }

  getOutputPorts = (node) => {
    let outputPorts = this.state.fullContent.filter(quad => quad.subject.id.includes(node) && quad.predicate.id.includes("maxOutPorts")).map(i => i.object.id);
    return outputPorts;
  }

  getMessage = (node) => {
    let message = this.state.fullContent.filter(quad => quad.subject.id.includes(node) && quad.predicate.id.includes("comment")).map(i => i.object.id);
    return message;
  }

  getUrlForTheNode = (node) => {
    let message = this.state.fullContent.filter(quad => quad.subject.id.includes(node) && quad.predicate.id.includes("seeAlso")).map(i => i.object.id);
    return message;
  }

  initializeNode = (node) => {   
    let propsArrForNode = this.getPropertiesForNode(node);
    let inputPorts = this.getInputPorts(node)[0];
    let outputPorts = this.getOutputPorts(node)[0];
    let inputs = inputPorts.match(/\d+/)[0];
    let outputs = outputPorts.match(/\d+/)[0];
    let message = this.getMessage(node);
    let url = this.getUrlForTheNode(node);
    let xoneProperties = null;
    let propsSelector = propsArrForNode.propsSelector;
    let that = this;

    let properties = {
      name: ""
    };

    let filteredProps = propsArrForNode.basicProps.map(filteredProp => {
      return this.getPropertyName(filteredProp);
    })

    filteredProps.forEach(pr => {
      properties[pr] = "";
    });

    // now all fields from xone are added, show only one to the user (with radiobutton)
    filteredProps = propsArrForNode.xone.filter(x => x.includes(node)).map(filteredProp => {
      return this.getPropertyName(filteredProp);
    })
    xoneProperties = filteredProps;

    filteredProps.forEach(pr => {
      properties[pr] = "";
    });

    propsSelector.propsMaxCount.forEach(pr => {
      properties[pr.nodeSelectorProp] = "";
    })
  
    // add to graph
    if(node.includes("Operator") ){
      class nodeClass extends FactoryNode{
        constructor(props) {
          super(props);
          for(var i = 0; i < inputs; i++){
            this.addInput("input", "text");
          }
          for(var j = 0; j < outputs; j++){
            this.addOutput("output", "text");
          }
          
          this.message = message;
          this.linkName = url;

          this.onDblClick = (e) => {
            that.showProperies("Operator/", e, node, properties, xoneProperties, propsSelector);
          }
        }
      };
      nodeClass.title = node;
      nodeClass.size = [320, 100];
      nodeClass.color = "#664d00";
      nodeClass.bgcolor = "#8c6a00";
      litegraph.registerNodeType("Operator/"+node, nodeClass);
      
    } else 
    if(node.includes("Reader")){
      // console.log(typeof properties);
      // console.log(Object.create(properties));
      class nodeClass extends FactoryNode{
        constructor(props) {
          super(props);
          this.addOutput("output", "text");
          this.message = message;
          this.linkName = url;

          this.onDblClick = (e) => {
            that.showProperies("Reader/", e, node, properties, xoneProperties, propsSelector);
          }

        }
      };
      nodeClass.title = node;
      nodeClass.size = [180, 100];
      nodeClass.color = "#223322";
      nodeClass.bgcolor = "#335533";
      litegraph.registerNodeType("Reader/"+node, nodeClass);
    } else {
      class nodeClass extends FactoryNode{
        constructor(props) {
          super(props);
          this.addInput("input", "text");
          this.message = message;
          this.linkName = url;

          this.onDblClick = (e) => {
            that.showProperies("Writer/", e, node, properties, xoneProperties, propsSelector);
          }
        }
      };
      nodeClass.title = node;
      nodeClass.size = [180, 100];
      nodeClass.color = "#223322";
      nodeClass.bgcolor = "#335533";
      litegraph.registerNodeType("Writer/"+node, nodeClass);
    }
  }

  showProperies = (nodeType, e, node, properties, xoneProperties, propsSelector) => {
    let propertiesCopy = Object.assign({}, properties);
    // initialize panel data
    let panelData = Object.assign([], this.state.panelData);
    // hide all panels
    panelData = panelData.map(p => {
      let temp = Object.assign({}, p);
      temp.showPanel = false;
      return temp;
    });
    // if this panel was just added
    let exists = this.state.panelData.find(p => { return p.numNodeType === e.target.data.current_node.id && p.nodePath === nodeType+node});
    if(!exists){
      panelData.push({
          numNodeType: e.target.data.current_node.id,
          nodePath: nodeType+node,
          properties: propertiesCopy,
          xoneProperties: xoneProperties,
          showPanel: true,
          propsSelector: propsSelector,
      });
      this.setState({
        panelData: panelData,
      });
    }  
    else {
      // show current one
      let idForChange = this.state.panelData.findIndex(p => { return p.nodePath === nodeType+node && p.numNodeType === e.target.data.current_node.id});
      panelData[idForChange].showPanel = true;
      this.setState({
        panelData: panelData,
      });
    }
  }

  showNode = (quadOb) => {
    // save possible node names in array
    if(quadOb.predicate.id.includes("targetClass")){
      let node = quadOb.object.id.split("https://w3id.org/deer/")[1];
      // console.log(quadOb.object.id);
      let nodes = this.state.nodesArray;
      if(node){
        nodes.add(node);
        this.initializeNode(node);
      }

      this.setState({
        nodesArray: nodes,
      });
      
    }
  };

  toggleDropdown = () => {
    this.setState({
      show: !this.state.show,
    });
  };

  // getOutputLinks = (node) => {
  //   for (let link in node.outputs) {
  //     if (node.outputs[link].links) {
  //       for (let nodeLink in node.outputs[link].links) {
  //         var link_id = node.outputs[link].links[nodeLink];
  //         var linkInGraph = this.state.graph.links[link_id];
  //         if (linkInGraph) {
  //           var target_node = this.state.graph.getNodeById(
  //             linkInGraph.target_id
  //           );
  //           return target_node;
  //         }
  //       }
  //     }
  //   }
  // };

  saveConfig = () => {

    var data = this.state.graph.serialize();
    //use N3
    const myQuad = DataFactory.quad(
      namedNode("https://ruben.verborgh.org/profile/#me"),
      namedNode("http://xmlns.com/foaf/0.1/givenName"),
      literal("Ruben", "en"),
      defaultGraph()
    );

    var writer = new N3.Writer(
      {
        prefixes: this.state.prefixes,
      },
      { format: "N-Triples" }
    );

    var parser = new N3.Parser({ format: "N3", blankNodePrefix: "" });
    data.nodes.map((node, key) => {
      let inputPorts = [];
      let curInputLinkId = '';

      node.properties = this.state.panelData.filter(i => i.numNodeType === node.id && i.nodePath === node.type)[0].properties;

      writer.addQuad(
        namedNode("urn:example:demo/" + node.properties.name),
        namedNode("http://www.w3.org/1999/02/22-rdf-syntax-ns#type"), //predicate
        namedNode("https://w3id.org/deer/" + node.type.split("/")[1]) //object
      );

      //if it has an input link, check for them and add to an array
      if (node.inputs) {
          for (var i=0; i < node.inputs.length; i++) {
            if (node.inputs[i].link) {
              var inputLinkId = node.inputs[i].link;
              var inputLinkInGraph = this.state.graph.links[inputLinkId];
             if (inputLinkInGraph) {
               var inputOriginNode = this.state.graph.getNodeById(
                 inputLinkInGraph.origin_id
               );
               let inputPortsList = this.state.inputPorts;
                 inputPortsList.push(inputOriginNode);
                 inputPorts = inputPortsList;
                 curInputLinkId = inputLinkId
              
             }
         }
         }
          // console.log(this.state.inputPorts);
          //adding the quad for each inputLinks here
          let blankNodes = inputPorts.map((inputPort, key) => {
            inputPort.properties = this.state.panelData.filter(i => i.numNodeType === inputPort.id && i.nodePath === inputPort.type)[0].properties;
            return writer.blank([{
              predicate: namedNode("https://w3id.org/fcage/" + "fromNode"),
              object:    namedNode("urn:example:demo/" + inputPort.properties.name),
            },{
              predicate: namedNode("https://w3id.org/fcage/" + "fromPort"),
              object:    literal(key),
            }])
          })

          
          writer.addQuad(
            namedNode("urn:example:demo/" + node.properties.name),
            namedNode("https://w3id.org/fcage/" + "hasInput"),
            writer.list(blankNodes)        
          );
          
          inputPorts.length = 0;
          curInputLinkId = '';

      }

      // console.log(node);

      let obj = node.properties;
      // quads with blank nodes
      if ("operation" in obj){
        let blankNodes = this.addBlankNodes("operation", obj);
        blankNodes.forEach(i => {
          writer.addQuad(
            namedNode("urn:example:demo/" + obj.name),
            namedNode("https://w3id.org/deer/operation"),
            writer.blank(i)
          );
        })
      } else if ("selector" in obj && obj.selector !== ""){
        let blankNodes = this.addBlankNodes("selector", obj);
        blankNodes.forEach(i => {
          writer.addQuad(
            namedNode("urn:example:demo/" + obj.name),
            namedNode("https://w3id.org/deer/selector"),
            writer.blank(i)
          );
        })
      }
      else{ // simple quads
        for (var prop in obj) {
          if (prop !== "name" && obj[prop].length){
            if (obj[prop].includes('http')){
              writer.addQuad(
                namedNode("urn:example:demo/" + node.properties.name),
                namedNode("https://w3id.org/deer/" + prop),
                namedNode(obj[prop])
              );
            }
            else {
              writer.addQuad(
                namedNode("urn:example:demo/" + node.properties.name),
                namedNode("https://w3id.org/deer/" + prop),
                literal(obj[prop])
              );
            }
          }
        }
      }


    });
    return writer;
  }

  downloadTtl = () => {
    let writer = this.saveConfig();
    writer.end((error, result) => {
      console.log(result);
      let blob = new Blob([result], { type: "text/ttl" });
      let url = window.URL.createObjectURL(blob);
      let a = document.createElement("a");
      a.href = url;
      a.download = "config.ttl";
      a.click();
      result = "";
    });
  }

  runConfig = () => {
    let writer = this.saveConfig();
    writer.end((error, result) => {
      console.log(result);
      this.submitConfig(result);
      result = "";
    });
  };

  sortBySelectorValue = (propsSelector) => {
    const propsSelectorSorted = [];
    let predPropPair = [];
    propsSelector.forEach(i => {
      let num;
      if(!i.match(/_extra_\d+/g))
        num = i.match(/\d+/g) || 1;
      else
        num = i.split("_extra_")[0].match(/\d+/g);
      let matchedProps;  
      if(num === 1 || num === null){
        matchedProps = propsSelector.filter(p => p.match(/\d+/g) === null || (p.match(/_extra_\d+/g) && !p.split("_extra_")[0].match(/\d+/g)));
      } else {
        matchedProps = propsSelector.filter(p => (p.includes(num) && !p.match(/_extra_\d+/g)) || (p.match(/_extra_\d+/g) && p.split("_extra_")[0].match(/\d+/g) && parseInt(p.split("_extra_")[0].match(/\d+/g)[0]) === parseInt(num)));
      }

      let exists = propsSelectorSorted.map(i => !i.every(elem => matchedProps.includes(elem)));
      let containsFalse = exists.includes(false)

      if(matchedProps.length && !containsFalse){
        propsSelectorSorted.push(matchedProps);
      }

      predPropPair = matchedProps;
    })
    propsSelector = propsSelectorSorted;
    return propsSelector;
  }

  to2dArray = (obj) => {
    let propsSelector = Object.keys(obj);
    propsSelector = propsSelector.filter(e => { return e !== 'name' })
    propsSelector = this.sortBySelectorValue(propsSelector);
    return propsSelector;
  }

  addBlankNodes = (byPredicate, obj) => {
    let arr2D = this.to2dArray(obj);
    let blankNodes = [];

    arr2D.forEach(i => {
      let subBlankNodes = [];
      i.forEach(j => {
        if (j !== "name" && j !== byPredicate && obj[j].length){
          subBlankNodes.push({
            predicate: namedNode(
              "https://w3id.org/deer/"+j.replace(/\d+/g, '').replace("_extra_", "")
            ),
            object: namedNode(obj[j])
          });
        }
      })
      blankNodes.push(subBlankNodes);
    })
    return blankNodes;
  }

  //Download the results
  downloadResults = (index) => {
    fetch(
      URI +
        "/result/" +
        this.state.requestID +
        "/" +
        this.state.availableFiles[index]
    ).then((response) => {
      response.blob().then((blob) => {
        let url = window.URL.createObjectURL(blob);
        let a = document.createElement("a");
        a.href = url;
        a.download = this.state.availableFiles[index];
        a.click();
      });
    });
  };

  submitConfig = (result) => {
    var data = new Blob([result], { type: "text/ttl" });
    var file = new File([data], "config.ttl");

    var formData = new FormData();
    formData = this.uploadFiles();
    formData.append("config", file);
    fetch(URI + "/submit", {
      method: "POST",
      body: formData,
    })
      .then((response) => response.json())
      .then((res) => {
        if (res.error && res.error.code === -1) {
          this.setState({
            visible: true,
          });
        } else if (res.requestId) {
          this.setState({
            requestID: res.requestId,
            showLogButton: true,
          });
          this.interval = setInterval(this.getStatusForRequest, 1000);
        }
      });
  };

  getStatusForRequest = () => {
    fetch(URI + "/status/" + this.state.requestID)
      .then(function (response) {
        return response.json();
      })
      .then((content) => {
        if (content.status.code === 2) {
          clearInterval(this.interval);
          this.setState({
            requestCompleteModal: true,
            showConfigButton: true,
          });
          this.getResults();
        } else if(content.status.code === 1){
          clearInterval(this.interval);
          this.setState({
            visible: true,
          });
        }
      });
  };

  getResults = () => {
    fetch(URI + "/results/" + this.state.requestID)
      .then(function (response) {
        return response.json();
      })
      .then((content) => {
        console.log(content.availableFiles);
        this.setState({
          availableFiles: content.availableFiles,
        });
      });
  };

  showLogs = () => {
    fetch(URI + "/logs/" + this.state.requestID).then(function (response) {
      let a = document.getElementById("downloadlink");
      a.href = response.url;
      a.click();
    });
  };
  toggle = () => {
    this.setState({
      visible: !this.state.visible,
    });
  };

  getFilteredOptions = (event) => {
    this.setState({
      afterFilteredSuggestions: this.state.prefixOptions.filter((i) => {
        return i.toLowerCase().includes(event.target.value.toLowerCase());
      }),
      userInput: event.target.value,
    });
  };

  selectedPrefix = (event) => {
    this.setState({
      afterFilteredSuggestions: [],
      namespace: this.state.contexts[event.target.value],
      userInput: event.target.value,
      isDisabled: false,
    });
  };

  handleNamespaceChange = (event) => {
    this.setState({
      namespace: event.target.value,
      isDisabled: false,
    });
  };

  addNewPrefixes = (e) => {
    this.setState({
      addNewPrefixes: this.state.addNewPrefixes.concat(this.state.userInput),
    });
    this.state.prefixes[this.state.userInput] = this.state.namespace;
  };

  saveResults = () => {
    this.setState({
      showResultModal: !this.state.showResultModal,
    });
  };

  toggleResultModal = () => {
    this.setState({
      showResultModal: !this.state.showResultModal,
    });
  };

  toggleRequestCompleteModal = () => {
    this.setState({
      requestCompleteModal: !this.state.requestCompleteModal,
    });
  };

  uploadFiles = () => {
    let files = this.state.selectedFiles;

    const data = new FormData()
    for(var x = 0; x < files.length; x++) {
       data.append('file', files[x])
    }

    return data;
  };

  onSelectedFiles = (event) => {
    this.setState({
     selectedFiles: event.target.files,
    })
  };

  render() {
    const options = _.map(this.state.prefixOptions, (opt, index) => ({
      key: opt,
      text: opt,
      value: opt,
    }));

    return (
      <div className="content">
        <Modal isOpen={this.state.visible} toggle={this.toggle}>
          <ModalHeader toggle={this.toggle}>
            Incorrect Configuration
          </ModalHeader>
          <ModalBody>
            Please input all the required fields and connections.
          </ModalBody>
        </Modal>

        <Modal
          isOpen={this.state.requestCompleteModal}
          toggle={this.toggleRequestCompleteModal}
        >
          <ModalHeader toggle={this.toggleRequestCompleteModal}>
            Status
          </ModalHeader>
          <ModalBody>
            The results are ready. You can download them by clicking 'Show
            Results'.
          </ModalBody>
        </Modal>

        <Modal
          isOpen={this.state.showResultModal}
          toggle={this.toggleResultModal}
        >
          <ModalHeader toggle={this.toggleResultModal}>Results</ModalHeader>
          <ModalBody>
            <Table>
              <thead>
                <tr>
                  <th>File</th>
                  <th>Download</th>
                </tr>
              </thead>
              <tbody>
                {this.state.availableFiles.map((file, index) => (
                  <tr key={index}>
                    <td>{file}</td>
                    <td>
                      <Button onClick={() => this.downloadResults(index)}>
                        <i
                          className="fa fa-download"
                          style={{ color: `white` }}
                        />{" "}
                        Download
                      </Button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </Table>
          </ModalBody>
        </Modal>
        <Row>
          <Col lg="9" md="9" sm="9">
            <Card className="card-stats">
              <div className="numbers">
                <CardTitle tag="p">Prefixes</CardTitle>
              </div>
              <CardBody>
                <Row>
                  <Col md="12" xs="12">
                    <Form>
                      <Row>
                        <Col md="4">
                          <Fragment>
                            <FormGroup className="dropdown">
                              <label>Label</label>
                              <Input
                                placeholder="Label"
                                type="text"
                                value={this.state.userInput}
                                onChange={this.getFilteredOptions}
                              ></Input>
                              <Dropdown
                                className="dropdown-content"
                                toggle={this.toggleDropdown}
                              >
                                {this.state.afterFilteredSuggestions.map(
                                  (suggestions, key) => (
                                    <DropdownItem
                                      className="dropdown-item"
                                      value={suggestions}
                                      onClick={this.selectedPrefix}
                                    >
                                      {suggestions}
                                    </DropdownItem>
                                  )
                                )}
                              </Dropdown>
                            </FormGroup>
                          </Fragment>
                        </Col>
                        <Col md="4">
                          <FormGroup>
                            <label>Namespace</label>
                            <Input
                              placeholder="Namespace"
                              type="text"
                              value={this.state.namespace}
                              onChange={this.handleNamespaceChange}
                            />
                          </FormGroup>
                        </Col>
                        <Col md="4">
                          <Button
                            disabled={this.state.isDisabled}
                            className="btn-round prefixBtn"
                            color="primary"
                            onClick={this.addNewPrefixes}
                          >
                            Add prefix
                          </Button>
                        </Col>
                      </Row>
                    </Form>
                  </Col>
                </Row>
              </CardBody>
              <CardFooter>
                <hr />
                <div className="stats">
                  {this.state.addNewPrefixes.map((addPrefix) => (
                    <Badge variant="light"> {addPrefix}</Badge>
                  ))}
                </div>
              </CardFooter>
            </Card>
          </Col>
          <Col lg="3" md="3" sm="3">
            <Card className="card-stats">
              <div className="numbers">
                <CardTitle tag="p">Attach files</CardTitle>
              </div>
              <CardBody>
                {/* <div class="form-group">
                  <input
                    type="file"
                    class="form-control-file"
                    id="exampleFormControlFile1"
                  ></input>
                </div> */}
                <Row>
                  <Col md="8">
                    <Input className="file inputFile" multiple type="file" name="file" id="exampleFile" onChange={this.onSelectedFiles}/>
                  </Col>
                </Row>
              </CardBody>
              <hr />
              <CardFooter></CardFooter>
            </Card>
          </Col>
        </Row>
        <Row>
          <Col md="9">
            <Card>     
              <div className="numbers">
                <CardTitle tag="p">Graph</CardTitle>
              </div> 
              <CardBody>
                <div id="parentCanvas" className="litegraph litegraph-editor">
                  <canvas id="mycanvas" height="600" width="1000"></canvas>{" "}
                </div>
              </CardBody>
              <CardFooter>
                <hr />
                <div className="stats">
                  {/* <a
                    download="config.ttl"
                    id="downloadlink"
                    //style={{ display: "none" }}
                    ref="file"
                  > */}
                  <Button onClick={this.runConfig}>
                    <i className="fa fa-cog" style={{ color: `white` }} /> Run
                    Configuration
                  </Button>
                  {/* </a> */}
                  {this.state.showConfigButton ? (
                    <Button
                      onClick={this.saveResults}
                      style={{ marginLeft: `10px` }}
                    >
                      <i
                        className="fa fa-sticky-note"
                        style={{ color: `white` }}
                      />{" "}
                      Show results
                    </Button>
                  ) : (
                    ""
                  )}
                  {this.state.showLogButton ? (
                    <a
                      target="_blank"
                      id="downloadlink"
                      //style={{ display: "none" }}
                      ref="file"
                    >
                      <Button
                        onClick={this.showLogs}
                        style={{ marginLeft: `10px` }}
                      >
                        <i className="fa fa-cog" style={{ color: `white` }} />{" "}
                        Logs
                      </Button>
                    </a>
                  ) : (
                    ""
                  )}
                  <Button onClick={this.downloadTtl} style={{ marginLeft: `10px` }}>
                    <i className="fa fa-download" style={{ color: `white` }} /> Export
                    Configuration
                  </Button>
                </div>
              </CardFooter>
            </Card>
          </Col>
          {/* <Modal isOpen={this.state.isModalOpen} toggle={this.toggle}>
            <ModalHeader toggle={this.toggle}>Display Config</ModalHeader>
            <ModalBody></ModalBody>
          </Modal> */}

          <Col md="3">
            {this.state.panelData.filter(p => p.showPanel === true).map((p) =>(
            <Panel key={p.nodePath+p.numNodeType} panelData={p} updateParentPanelData={this.updateParentPanelData} sortBySelectorValue={this.sortBySelectorValue} />))} 
          </Col>

        </Row>
        {/* <Row>
            <Col md="4">
              <Card>
                <CardHeader>
                  <CardTitle tag="h5">Email Statistics</CardTitle>
                  <p className="card-category">Last Campaign Performance</p>
                </CardHeader>
                <CardBody>
                  <Pie
                    data={dashboardEmailStatisticsChart.data}
                    options={dashboardEmailStatisticsChart.options}
                  />
                </CardBody>
                <CardFooter>
                  <div className="legend">
                    <i className="fa fa-circle text-primary" /> Opened{" "}
                    <i className="fa fa-circle text-warning" /> Read{" "}
                    <i className="fa fa-circle text-danger" /> Deleted{" "}
                    <i className="fa fa-circle text-gray" /> Unopened
                  </div>
                  <hr />
                  <div className="stats">
                    <i className="fa fa-calendar" /> Number of emails sent
                  </div>
                </CardFooter>
              </Card>
            </Col>
            <Col md="8">
              <Card className="card-chart">
                <CardHeader>
                  <CardTitle tag="h5">NASDAQ: AAPL</CardTitle>
                  <p className="card-category">Line Chart with Points</p>
                </CardHeader>
                <CardBody>
                  <Line
                    data={dashboardNASDAQChart.data}
                    options={dashboardNASDAQChart.options}
                    width={400}
                    height={100}
                  />
                </CardBody>
                <CardFooter>
                  <div className="chart-legend">
                    <i className="fa fa-circle text-info" /> Tesla Model S{" "}
                    <i className="fa fa-circle text-warning" /> BMW 5 Series
                  </div>
                  <hr />
                  <div className="card-stats">
                    <i className="fa fa-check" /> Data information certified
                  </div>
                </CardFooter>
              </Card>
            </Col>
          </Row> */}
      </div>
    );
  }
}

litegraph.registered_node_types = {};
litegraph.searchbox_extras = {};

export default Dashboard;
