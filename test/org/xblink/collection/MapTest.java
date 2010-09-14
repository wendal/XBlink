package org.xblink.collection;

import static org.junit.Assert.assertTrue;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Ignore;
import org.junit.Test;
import org.xblink.XBlink;
import org.xblink.collection.map.KeyObject;
import org.xblink.collection.map.MapContainer;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.XppDriver;

public class MapTest {
	
	private String mc1 = "C:/Map1.xml";
	private String mc2 = "C:/Map2.xml";
	private String mc7 = "C:/Map7.xml";
	
	static private MapContainer getMapContainer(){
		return new MapContainer();
	}
	
	static private KeyObject getKeyObject(){
		return new KeyObject();
	}
	
	static private KeyObject getKeyObject(String keyValue1,String keyValue2){
		KeyObject key = getKeyObject();
		key.setKeyValue1(keyValue1);
		key.setKeyValue2(keyValue2);
		return key;
	}
	
	@Test
	public void testMap1() throws Exception {
		MapContainer mc = getMapContainer();
		
		Map<Integer, String> map1 = new HashMap<Integer, String>();
		map1.put(new Integer(3), "This is 3");
		map1.put(new Integer(5), "This is 5");
		map1.put(new Integer(8), "This is 8");
		
		mc.setMap1(map1);
		
		XBlink.toXml(mc1, mc);
		
		// XStream
		XStream xStream1 = new XStream(new XppDriver());
		xStream1.toXML(mc, new BufferedOutputStream(new FileOutputStream(new File(
		"C:/Map1_XStream.xml"))));
		
		MapContainer re = (MapContainer) XBlink.fromXml(mc1, MapContainer.class);
		
		assertTrue(re.getMap1() != null);
		Map<Integer, String> remap1 = re.getMap1();
		assertTrue(remap1.get(new Integer(3)).equals("This is 3"));
		assertTrue(remap1.get(new Integer(5)).equals("This is 5"));
		assertTrue(remap1.get(new Integer(8)).equals("This is 8"));
	}
	
	@Test
	public void testMap2() throws Exception {
		
		MapContainer mc = getMapContainer();
		
		Map<KeyObject, String> map2 = new HashMap<KeyObject, String>();
		KeyObject keyObject1 = getKeyObject("测试数据1", "测试数据2");
		KeyObject keyObject2 = getKeyObject("测试数据3", "测试数据4");
		map2.put(keyObject1, "key1");
		map2.put(keyObject2, "key2");
		
		mc.setMap2(map2);
		
		XBlink.toXml(mc2, mc);
		
		// XStream
		XStream xStream1 = new XStream(new XppDriver());
		xStream1.toXML(mc, new BufferedOutputStream(new FileOutputStream(new File(
				"C:/Map2_XStream.xml"))));
		
		MapContainer re = (MapContainer) XBlink.fromXml(mc2, MapContainer.class);

		assertTrue(re.getMap2() != null);
		Map<KeyObject, String> remap2 = re.getMap2();
		Set<KeyObject> keySet = remap2.keySet();
		for(KeyObject ko : keySet){
			String value = remap2.get(ko);
			if("key1".equals(value)){
				assertTrue("测试数据1".equals(ko.getKeyValue1()));
				assertTrue("测试数据2".equals(ko.getKeyValue2()));
			}
			if("key2".equals(value)){
				assertTrue("测试数据3".equals(ko.getKeyValue1()));
				assertTrue("测试数据4".equals(ko.getKeyValue2()));
			}
		}
		
	}

	@Ignore
	@Test
	public void testMap7() throws Exception {
		
		MapContainer mc = getMapContainer();
		
		Map<KeyObject, String> map2 = new HashMap<KeyObject, String>();
		KeyObject keyObject1 = getKeyObject("测试数据1", "测试数据2");
		KeyObject keyObject2 = getKeyObject("测试数据3", "测试数据4");
		map2.put(keyObject1, "key1");
		map2.put(keyObject2, "key2");
		
		mc.setMap7(map2);
		
		XBlink.toXml(mc7, mc);
		
		// XStream
		XStream xStream1 = new XStream(new XppDriver());
		xStream1.toXML(mc, new BufferedOutputStream(new FileOutputStream(new File(
		"C:/Map7_XStream.xml"))));
		
		MapContainer re = (MapContainer) XBlink.fromXml(mc7, MapContainer.class);
		
		assertTrue(re.getMap7() != null);
		Map remap7 = re.getMap7();
		assertTrue(remap7.get(keyObject1).equals("key1"));
		assertTrue(remap7.get(keyObject2).equals("key2"));
	}
	
	

}
