package de.hochschuletrier.gdw.commons.ai.behaviourtree.nodes;

import java.util.Collections;

public class ShuffleSelector extends Selector {

	public ShuffleSelector(BaseNode parent) {
		super(parent);
	}

	@Override
	public void activate() {
		if (this.children.size() > 0) {
			Collections.shuffle(this.children);
			this.currentChild = 0;
			this.children.get(this.currentChild).activate();
		}
	}
}
