package org.apache.poi.xdgf.usermodel;

import com.microsoft.schemas.office.visio.x2012.main.ConnectType;

public class XDGFConnection {

	// comments on frompart/topart taken from pkgVisio 
	
    //    The following constants declared by the Microsoft Office Visio type 
    //       library show return values for the FromPart property.
    // Constant                  Value  
    // visConnectFromError         -1
    // visFromNone                 0
    // visLeftEdge                 1
    // visCenterEdge               2
    // visRightEdge                3
    // visBottomEdge               4
    // visMiddleEdge               5
    // visTopEdge                  6
    // visBeginX                   7
    // visBeginY                   8
    // visBegin                    9
    // visEndX                     10
    // visEndY                     11
    // visEnd                      12
    // visFromAngle                13
    // visFromPin                  14
    // visControlPoint             100 + zero-based row index (for example, visControlPoint = 100 if the control point is in row 0; visControlPoint = 101 if the control point is in row 1)

	//  The ToPart property identifies the part of a shape to which another 
	//    shape is glued, such as its begin point or endpoint, one of its edges, 
	//    or a connection point. The following constants declared by the Visio type library in member VisToParts show possible return values for the ToPart property.
	// Constant               Value  
	// visConnectToError       -1
	// visToNone               0
	// visGuideX               1
	// visGuideY               2
	// visWholeShape           3
	// visGuideIntersect       4
	// visToAngle              7
	// visConnectionPoint      100 + row index of connection point
	
	private ConnectType _connect;
	private XDGFShape _from;
	private XDGFShape _to;
	
	
	public XDGFConnection(ConnectType connect, XDGFShape from, XDGFShape to) {
		_connect = connect;
		_from = from;
		_to = to;
	}
	
	public XDGFShape getFromShape() {
		return _from;
	}
	
	public XDGFCell getFromCell() {
		return _from.getCell(_connect.getFromCell());
	}
	
	public String getFromCellName() {
		return _connect.getFromCell();
	}
	
	public XDGFShape getToShape() {
		return _to;
	}
	
	public String getToCellName() {
		return _connect.getToCell();
	}
	
}
