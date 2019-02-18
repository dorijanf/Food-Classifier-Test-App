package com.wuggs.imgclassifier;

import com.wuggs.imgclassifier.Classifier.Recognition;

import java.util.List;

public interface ResultsView {
  public void setResults(final List<Recognition> results);
}
