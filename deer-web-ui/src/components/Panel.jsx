import React, { Fragment } from "react";
import { Link } from "react-router-dom";
import { v4 as uuidv4 } from 'uuid';
import {
  Collapse,
  Navbar,
  NavbarToggler,
  NavbarBrand,
  Container,
  Button,
  Card,
  CardTitle,
  CardBody,
  FormGroup,
  Input,
  Label,
} from "reactstrap";


class Panel extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      showSelectorProps: false,
      name: "",
    };
  }

  componentDidMount() {
    if(this.props.panelData.properties.name === ''){
      let name = uuidv4();
      var temp = Object.assign({}, this.props.panelData);
      temp.properties.name = name;
      this.props.updateParentPanelData(temp, this.props.panelData.numNodeType);
      this.setState({name: name});
    } else {
      this.setState({name: this.props.panelData.properties.name});
    }
  }

  changeInput = (e, p) => {
    var temp = Object.assign({}, this.props.panelData);
    let curVal = e.target.value;
    if(e.target.value.length > 1){
      curVal = e.target.value.trim();
    }
    temp.properties[p] = curVal;
    this.props.updateParentPanelData(temp, this.props.panelData.numNodeType);
  }

  changeInputWithRadio = (e, p) => {
    // change the edited input
    this.changeInput(e, p);

    var temp = Object.assign({}, this.props.panelData);
    let updatedProperties = temp.properties;
    let excludeProps = this.props.panelData.xoneProperties.filter(i => {return i !== undefined && i !== p});
    // clear all optional except the current one
    Object.keys(temp.properties).forEach(i => {
      excludeProps.forEach(exp => {
        if (exp === i){
          updatedProperties[i] = "";
        } 
      })     
    });
    temp.properties = updatedProperties;
    this.props.updateParentPanelData(temp, this.props.panelData.numNodeType);
  
  }

  changeRadio = (e, p) => {
    e.target.value = " ";
    this.changeInputWithRadio(e, p);
    if(p === "selector"){
      this.setState({
        showSelectorProps: true
      })
    } else {
      this.setState({
        showSelectorProps: false
      })
      // clear property inputs when another radio button was checked
      let temp = Object.assign({}, this.props.panelData);
      let updatedProperties = temp.properties;
      let excludeProps = this.props.panelData.xoneProperties.filter(i => {return i !== undefined && i !== p});
      // clear selectorProps if there is selector in xone and it is not chosen
      if(excludeProps.includes("selector")){
        let propsSelector = this.props.panelData.propsSelector.propsMaxCount.map(i => i.nodeSelectorProp);
        Object.keys(temp.properties).forEach(i => {
          propsSelector.forEach(exp => {
            if (exp === i){
              updatedProperties[i] = "";
            } 
          })     
        });
      }
      temp.properties = updatedProperties;
      this.props.updateParentPanelData(temp, this.props.panelData.numNodeType);
    }
  }

  addOneMoreProperty = (p) => {
    let curProp = p;
    p = p.replace(/\d+/g, '').replace("_extra_", "");
    let propsSelector = this.props.panelData.propsSelector.propsMaxCount;
    let temp = Object.assign({}, this.props.panelData);
    let maxCount = propsSelector.filter(i => i.nodeSelectorProp === p)[0].maxCount;
    let preNums = propsSelector.filter(i => i.nodeSelectorProp.includes(p));
    let nums = preNums.filter(i => i.nodeSelectorProp.includes("_extra_") 
      && i.nodeSelectorProp.split("_extra_")[0] === curProp).map(i => i.nodeSelectorProp.split("_extra_")[1]);
    let lastNum = Math.max(...nums) + 1;
    if (nums.length === 0){
      lastNum = "1";
    }
    //check max count and add additionalProp
    if(parseInt(maxCount) >= parseInt(lastNum)+1){
      temp.propsSelector.propsMaxCount.push({nodeSelectorProp: curProp+"_extra_"+lastNum, maxCount: maxCount});
      temp.properties[curProp+"_extra_"+lastNum] = "";
      this.props.updateParentPanelData(temp, this.props.panelData.numNodeType);
    } else {
      alert("maxCount: " + maxCount);
    }
  }

  addOneMorePropertyBundle = (pArr) => {
    pArr = pArr[0];
    let isMaxCount = false;
    let maxCount = -1;
    for (let i = 0; i < pArr.length; i++) {
      let p = pArr[i];
      if(!p.includes("_extra_")){
        p = p.replace(/\d+/g, '');
        let propsSelector = this.props.panelData.propsSelector.propsMaxCount;
        let temp = Object.assign({}, this.props.panelData);
        maxCount = propsSelector.filter(i => i.nodeSelectorProp === p)[0].maxCount;
        let preNums = propsSelector.filter(i => i.nodeSelectorProp.includes(p) && i.nodeSelectorProp.match(/\d+/g));
        let nums = preNums.map(i => i.nodeSelectorProp.match(/\d+/g)[0]);
        let lastNum = Math.max(...nums) + 1;
        if (nums.length === 0){
          lastNum = "2";
        }

        temp.propsSelector.propsMaxCount.push({nodeSelectorProp: p+lastNum, maxCount: maxCount});
        temp.properties[p+lastNum] = "";
        this.props.updateParentPanelData(temp, this.props.panelData.numNodeType);
      }
    }
  }


  render() {
    let excludeProps = this.props.panelData.xoneProperties.filter(i => {return i !== undefined});
    let propsSelectorWithMaxCount = this.props.panelData.propsSelector.propsMaxCount;
    let propsSelector = propsSelectorWithMaxCount.map(i => i.nodeSelectorProp);
    let otherProps = Object.keys(this.props.panelData.properties)
                    .filter(otherpr => !excludeProps.includes(otherpr) 
                      && !propsSelector.includes(otherpr) && otherpr !== "operation" && otherpr != "name");
    if(excludeProps.includes("selector") && this.state.showSelectorProps === false && this.props.panelData.properties["selector"].length == 0){
      propsSelector = [];
    }

    if(propsSelector.length){
      propsSelector = this.props.sortBySelectorValue(propsSelector);
    }

    return (
      <div>
        <Card>
          <div className="numbers">
            <CardTitle tag="p" className="nodeName">{this.props.panelData.nodePath.split('/')[1]}</CardTitle>
            <p className="nodePath">{this.props.panelData.nodePath}</p>
          </div> 
          <CardBody>
            <p>Properties</p>
            {otherProps.map((p) => (
              <Fragment key={p}>
                <FormGroup>
                  <label>{p}</label>
                  <Input
                    type="text"
                    value={this.props.panelData.properties[p]}
                    onChange={(e) => this.changeInput(e,p)}
                  ></Input>
                </FormGroup>
              </Fragment>
            ))}
            {excludeProps.map((pr) => (<FormGroup check key={pr}>
                <label>{pr}</label>
                <Label check>
                  <Input type="radio" checked={this.props.panelData.properties[pr].length !== 0} onChange={(e) => this.changeRadio(e,pr)} name="radio1" />
                  {pr !== "selector" ? <Input
                      type="text"
                      value={this.props.panelData.properties[pr]}
                      onChange={(e) => this.changeInputWithRadio(e,pr)}
                  ></Input> : <div className="shiftCheckbox"></div>}
                </Label>
            </FormGroup>))}
            {propsSelector.map((s, index) => (
              <div className="panelSelectorRow" key={index}>
                  {s.map((p) => (
                    <Fragment key={p}>
                      <FormGroup>
                        <label>{p}</label>
                        <div className="row">
                          <div className="col">                   
                            <Input
                              type="text"
                              value={this.props.panelData.properties[p]}
                              onChange={(e) => this.changeInput(e,p)}
                            ></Input>
                          </div>
                          {propsSelectorWithMaxCount.filter(i => i.nodeSelectorProp === p)[0].maxCount > 1 ? <Button className="plusButton" onClick={() => this.addOneMoreProperty(p)} color="primary">+</Button> : null}
                        </div>
                      </FormGroup>
                    </Fragment>
                  ))}
              </div>
            ))}
            {((this.props.panelData.properties.selector && this.props.panelData.properties.selector.length) || "operation" in this.props.panelData.properties) ? <Button className="plusButtonFixed" onClick={() => this.addOneMorePropertyBundle(propsSelector)} color="primary">+</Button>: null}
          </CardBody>
        </Card>
      </div>
    );
  }
}

export default Panel;
