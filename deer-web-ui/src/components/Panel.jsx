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
    // this.state = {
    // };
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
        console.log(exp, i);
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
  }

  addOneMoreProperty = (p) => {
    // todo
    // console.log(p);
  }


  render() {
    let excludeProps = this.props.panelData.xoneProperties.filter(i => {return i !== undefined});
    let propsSelector = this.props.panelData.propsSelector.map(i => i.nodeSelectorProp);
    let otherProps = Object.keys(this.props.panelData.properties).filter(otherpr => !excludeProps.includes(otherpr) && !propsSelector.includes(otherpr));
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
          </CardBody>
        </Card>
      </div>
    );
  }
}

export default Panel;
