package xy;

public class Xy {

	private String x;
	private String y;
	private String id;
	private String addr;
	
	public String getX() {
		return x;
	}
	public void setX(String x) {
		this.x = x;
	}
	public String getY() {
		return y;
	}
	public void setY(String y) {
		this.y = y;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAddr() {
		return addr;
	}
	public void setAddr(String addr) {
		this.addr = addr;
	}
	
	@Override
	public String toString() {
		return "Xy [x=" + x + ", y=" + y + ", id=" + id + ", addr=" + addr + "]";
	}
	
	
	
	
	
}
