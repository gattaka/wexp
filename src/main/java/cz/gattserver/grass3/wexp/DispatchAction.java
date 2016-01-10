package cz.gattserver.grass3.wexp;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

import cz.gattserver.grass3.wexp.in.impl.UI;

public interface DispatchAction extends Serializable {

	public UI dispatch(HttpServletRequest req);

}
