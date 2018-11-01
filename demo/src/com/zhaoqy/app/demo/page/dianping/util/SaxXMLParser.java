package com.zhaoqy.app.demo.page.dianping.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import com.zhaoqy.app.demo.page.dianping.item.City;

public class SaxXMLParser 
{
	private List<City> mCityList;

	public List<City> getListBySaxXMLParser(InputStream in) throws ParserConfigurationException, SAXException, IOException 
	{
		SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
		SAXParser saxParser = saxParserFactory.newSAXParser();
		MyHandler myHandler = new MyHandler();
		saxParser.parse(in, myHandler);
		return mCityList;
	}

	public class MyHandler extends DefaultHandler 
	{
		StringBuilder mStringBuilder;
		City mCity;

		@Override
		public void startDocument() throws SAXException 
		{
			mCityList = new ArrayList<City>();
			mStringBuilder = new StringBuilder();
		}

		@Override
		public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException 
		{
			if (localName.equals("City")) 
			{
				mCity = new City();
			}
			mStringBuilder.setLength(0);
		}

		@Override
		public void endDocument() throws SAXException 
		{
			super.endDocument();
		}

		@Override
		public void endElement(String uri, String localName, String qName) throws SAXException 
		{
			if (localName.equals("id")) 
			{
				mCity.setId(Integer.parseInt(mStringBuilder.toString()));
			} 
			else if (localName.equals("name")) 
			{
				mCity.setName(mStringBuilder.toString());
				
			} 
			else if (localName.equals("sortKey")) 
			{
				mCity.setSortKey(mStringBuilder.toString());
			} 
			else if (localName.equals("City")) 
			{
				mCityList.add(mCity);
			}
		}

		@Override
		public void characters(char[] ch, int start, int length) throws SAXException 
		{
			mStringBuilder.append(ch, start, length);
		}
	}
}
