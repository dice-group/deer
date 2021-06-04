import React, { Fragment } from "react";
import { Link } from "react-router-dom";
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
    };
  }

  changeInput = (e, p) => {
    var temp = Object.assign({}, this.props.panelData);
    temp.properties[p] = e.target.value;
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
    e.target.value = "type here";
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
        let propsSelector = this.props.panelData.propsSelector.map(i => i.nodeSelectorProp);
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
    p = p.replace(/\d+/g, '');
    let propsSelector = this.props.panelData.propsSelector;
    let temp = Object.assign({}, this.props.panelData);
    let maxCount = propsSelector.filter(i => i.nodeSelectorProp === p)[0].maxCount;
    let preNums = propsSelector.filter(i => i.nodeSelectorProp.includes(p) && i.nodeSelectorProp.match(/\d+/g));
    let nums = preNums.map(i => i.nodeSelectorProp.match(/\d+/g)[0]);
    let lastNum = Math.max(...nums) + 1;
    if (nums.length === 0){
      lastNum = "2";
    }
    //check max count and add additionalProp
    if(maxCount >= lastNum){
      temp.propsSelector.push({nodeSelectorProp: p+lastNum, maxCount: maxCount});
      temp.properties[p+lastNum] = "";
      this.props.updateParentPanelData(temp, this.props.panelData.numNodeType);
    } else {
      alert("maxCount: " + maxCount);
    }
  }


  render() {
    let excludeProps = this.props.panelData.xoneProperties.filter(i => {return i !== undefined});
    let propsSelector = this.props.panelData.propsSelector.map(i => i.nodeSelectorProp);
    let otherProps = Object.keys(this.props.panelData.properties).filter(otherpr => !excludeProps.includes(otherpr) && !propsSelector.includes(otherpr) && otherpr !== "operation");
    if(excludeProps.includes("selector") && this.state.showSelectorProps === false){
      propsSelector = [];
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
                    placeholder={p}
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
                  <Input
                      placeholder={pr}
                      type="text"
                      value={this.props.panelData.properties[pr]}
                      onChange={(e) => this.changeInputWithRadio(e,pr)}
                  ></Input>
                </Label>
            </FormGroup>))}
            {propsSelector.map((p) => (
              <Fragment key={p}>
                <FormGroup>
                  <label>{p}</label>
                  <div className="row">
                    <div className="col-md-9">
                      <Input
                        placeholder={p}
                        type="text"
                        value={this.props.panelData.properties[p]}
                        onChange={(e) => this.changeInput(e,p)}
                      ></Input>
                    </div>
                    <div className="col-md-3">
                      <Button className="plusButton" onClick={() => this.addOneMoreProperty(p)} color="primary">+</Button>
                    </div>
                  </div>
                </FormGroup>
              </Fragment>
            ))}
          </CardBody>
        </Card>
      </div>
    );
  }
}

export default Panel;
