
package edu.cs751homework4.service.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "getOrderResponse", namespace = "http://service.cs751homework4.edu/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getOrderResponse", namespace = "http://service.cs751homework4.edu/")
public class GetOrderResponse {

    @XmlElement(name = "return", namespace = "")
    private edu.cs751homework4.model.Order _return;

    /**
     * 
     * @return
     *     returns Order
     */
    public edu.cs751homework4.model.Order getReturn() {
        return this._return;
    }

    /**
     * 
     * @param _return
     *     the value for the _return property
     */
    public void setReturn(edu.cs751homework4.model.Order _return) {
        this._return = _return;
    }

}
