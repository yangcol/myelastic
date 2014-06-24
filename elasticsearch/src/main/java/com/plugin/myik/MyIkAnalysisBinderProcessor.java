package com.plugin.myik;

import org.elasticsearch.index.analysis.AnalysisModule;

public class MyIkAnalysisBinderProcessor extends AnalysisModule.AnalysisBinderProcessor{
	@Override
	public void processTokenFilters(TokenFiltersBindings tokenFiltersBindings) {
		 
    }
	
	 @Override
	 public void processAnalyzers(AnalyzersBindings analyzersBindings) {
	        //绑定ik插件的processer
	        analyzersBindings.processAnalyzer("myik", MyIkAnalysisProvider.class);
	        super.processAnalyzers(analyzersBindings);
	    }
	 
	 @Override
	 public void processTokenizers(TokenizersBindings tokenizerBindings) {
		 //tokenizerBindings.processTokenizer("myik", MyIkTokenizerFactory.class);
	 }
}
