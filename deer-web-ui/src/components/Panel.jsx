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
      panelData: [],
      excludeProps: [],
    };
  }

  changeInput = (e, p) => {
    var temp = Object.assign({}, this.props.panelData);
    temp.properties[p] = e.target.value;
    this.setState({
      panelData: temp,
    });
  }


  render() {
    let excludeProps = this.props.panelData.xoneProperties.filter(i => {return i !== undefined});   
    let otherProps = Object.keys(this.props.panelData.properties).filter(otherpr => !excludeProps.includes(otherpr));
    return (
      <div>
        <Card>
          <div className="numbers">
            <CardTitle tag="p">{this.props.panelData.nodePath.split('/')[1]}</CardTitle>
            <h4>{this.props.panelData.nodePath}</h4>
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
                      onChange={(e) => this.changeInput(e,pr)}
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
