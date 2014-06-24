package com.hint.auto;

public class DictParser {
	Type type;
	final int BASE_DICT_WORD = 0;
	final int BASE_DICT_S_COUNT = 1;
	final int BASE_DICT_H_COUNT = 2;
	final int BASE_DICT_ALIASES = 3;

	public DictParser() {
		this.type = Type.BASE_DICT;
	}

	public DictParser(Type tp) {
		this.type = tp;
	}

	Data parseLine(String text) {
		Data data = null;

		if (null == text) {
			return data;
		}

		switch (type) {
		case BASE_DICT:
			String[] sresult = text.split(",");
			if (sresult.length < 3) {
				data = new Data(sresult[BASE_DICT_WORD], 0, 0, null);
			} else {
				String[] aliases = new String[sresult.length
						- BASE_DICT_ALIASES];
				for (int i = 0; i < aliases.length; i++) {
					aliases[i] = sresult[BASE_DICT_ALIASES + i];
				}

				data = new Data(sresult[BASE_DICT_WORD],
						Integer.parseInt(sresult[BASE_DICT_S_COUNT]),
						Integer.parseInt(sresult[BASE_DICT_H_COUNT]), aliases);
			}
			break;
		default:
			data = null;
			break;
		}

		return data;
	}

	public enum Type {
		BASE_DICT,
	}
}
