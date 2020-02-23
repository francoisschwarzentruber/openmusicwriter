package musicwriter.guiswing.dialogs;

import java.awt.Point;
import java.awt.datatransfer.Transferable;

public class GhostDropEvent {
	private Point point;
	private Transferable transferable;

	public GhostDropEvent(Transferable transferable, Point point) {
		this.transferable = transferable;
		this.point = point;
	}

	public Transferable getTransferable() {
		return transferable;
	}

	public Point getDropLocation() {
		return point;
	}
}
