package com.teamtter.svg;

public interface SelectedPartListener {

	public void partSelected(String partKey, SelectedPartsState event);

	public void partUnSelected(String partKey, SelectedPartsState event);
}
