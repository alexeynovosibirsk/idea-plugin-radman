package com.nazarov.jaad.mp4.boxes;

import com.nazarov.jaad.mp4.MP4InputStream;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BoxImpl implements com.nazarov.jaad.mp4.boxes.Box {

	private final String name;
	protected long size, type, offset;
	protected com.nazarov.jaad.mp4.boxes.Box parent;
	protected final List<com.nazarov.jaad.mp4.boxes.Box> children;

	public BoxImpl(String name) {
		this.name = name;

		children = new ArrayList<com.nazarov.jaad.mp4.boxes.Box>(4);
	}

	public void setParams(com.nazarov.jaad.mp4.boxes.Box parent, long size, long type, long offset) {
		this.size = size;
		this.type = type;
		this.parent = parent;
		this.offset = offset;
	}

	protected long getLeft(MP4InputStream in) throws IOException {
		return (offset+size)-in.getOffset();
	}

	/**
	 * Decodes the given input stream by reading this box and all of its
	 * children (if any).
	 * 
	 * @param in an input stream
	 * @throws IOException if an error occurs while reading
	 */
	public void decode(MP4InputStream in) throws IOException {
	}

	public long getType() {
		return type;
	}

	public long getSize() {
		return size;
	}

	public long getOffset() {
		return offset;
	}

	public com.nazarov.jaad.mp4.boxes.Box getParent() {
		return parent;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return name+" ["+BoxFactory.typeToString(type)+"]";
	}

	//container methods
	public boolean hasChildren() {
		return children.size()>0;
	}

	public boolean hasChild(long type) {
		boolean b = false;
		for(com.nazarov.jaad.mp4.boxes.Box box : children) {
			if(box.getType()==type) {
				b = true;
				break;
			}
		}
		return b;
	}

	public com.nazarov.jaad.mp4.boxes.Box getChild(long type) {
		com.nazarov.jaad.mp4.boxes.Box box = null, b = null;
		int i = 0;
		while(box==null&&i<children.size()) {
			b = children.get(i);
			if(b.getType()==type) box = b;
			i++;
		}
		return box;
	}

	public List<com.nazarov.jaad.mp4.boxes.Box> getChildren() {
		return Collections.unmodifiableList(children);
	}

	public List<com.nazarov.jaad.mp4.boxes.Box> getChildren(long type) {
		List<com.nazarov.jaad.mp4.boxes.Box> l = new ArrayList<com.nazarov.jaad.mp4.boxes.Box>();
		for(com.nazarov.jaad.mp4.boxes.Box box : children) {
			if(box.getType()==type) l.add(box);
		}
		return l;
	}

	protected void readChildren(MP4InputStream in) throws IOException {
		com.nazarov.jaad.mp4.boxes.Box box;
		while(in.getOffset()<(offset+size)) {
			box = BoxFactory.parseBox(this, in);
			children.add(box);
		}
	}

	protected void readChildren(MP4InputStream in, int len) throws IOException {
		Box box;
		for(int i = 0; i<len; i++) {
			box = BoxFactory.parseBox(this, in);
			children.add(box);
		}
	}
}
