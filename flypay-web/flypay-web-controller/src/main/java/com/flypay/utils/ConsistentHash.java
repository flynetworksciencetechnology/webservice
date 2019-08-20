package com.flypay.utils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;


import org.apache.log4j.Logger;

public class ConsistentHash {
	private static final Logger LOGGER = Logger.getLogger(ConsistentHash.class);
	
	private static TreeMap<Long, String> nodes; // 虚拟节点
	private static List<String> servers;// 真实机器节点
	private final static int NODE_START = 1;
	private final static int NODE_NUM = 99; // 每个机器节点关联的虚拟节点个数
	public static void init() throws Exception { // 初始化一致性hash环
		nodes = new TreeMap<Long, String>();
		servers = new ArrayList<String>();
		/*Properties prop = new Properties();
		prop.load(Play.applicationPath.getPath()+"/conf/switch_db.properties");
		servers=prop.getKeys();*/
		int serviersize =0; //servers.size();
		int serial = NODE_START;
		List<String> nodelist = null;
		Map<String, List<String>> map  = new HashMap<String, List<String>>();
		for (int i = 0; i < servers.size(); i++) {
			nodelist = new ArrayList<String>();
			for(int j=0;j<NODE_NUM/serviersize;j++){
				serial++;
				nodelist.add(serial+"");
			}
			map.put(servers.get(i), nodelist);
		}	
		int nodenum = 10;//二级虚拟节点
		for (int i = NODE_START; i != (NODE_START+NODE_NUM); ++i) { // 每个真实机器节点都需要关联虚拟节点
			//final String shardInfo = shards.get(i);
			for (int n = 0; n < nodenum; n++){
				// 一个真实机器节点关联NODE_NUM个虚拟节点
				nodes.put(hash("SHARD-" + i + "-NODE-" + n), i+"");
			}
		}
		/*CreateXMLText saxxml = new CreateXMLText();
		saxxml.saxToXml(map);*/
		/*if(remainnum>0){//剩余节点分配到最后一个服务器上
			for (int j = 1; j < remainnum+1; j++) {
				nodes.put(hash("SHARD-" + (servernum-1) + "-NODE-" + (nodenum+j)),shards.get(servernum-1));
			}
		}*/
		LOGGER.info("一致哈希初始化完成============ ");
	}

	public static String getShardInfo(String key) {
		SortedMap<Long, String> tail = nodes.tailMap(hash(key)); // 沿环的顺时针找到一个虚拟节点
		if (tail.size() == 0) {
			return nodes.get(nodes.firstKey());
		}
		//System.out.println(tail.get(tail.firstKey())+"--tail--"+tail.firstKey());
		return tail.get(tail.firstKey()); // 返回该虚拟节点对应的真实机器节点的信息
	}

	public static Long hash(String key) {
		ByteBuffer buf = ByteBuffer.wrap(key.getBytes());
		int seed = 0x1234ABCD;
		ByteOrder byteOrder = buf.order();
		buf.order(ByteOrder.LITTLE_ENDIAN);
		long m = 0xc6a4a7935bd1e995L;
		int r = 47;
		long h = seed ^ (buf.remaining() * m);
		long k;
		while (buf.remaining() >= 8) {
			k = buf.getLong();
			k *= m;
			k ^= k >>> r;
			k *= m;
			h ^= k;
			h *= m;
		}

		if (buf.remaining() > 0) {
			ByteBuffer finish = ByteBuffer.allocate(8).order(
			ByteOrder.LITTLE_ENDIAN);
			// for big-endian version, do this first:
			// finish.position(8-buf.remaining());
			finish.put(buf).rewind();
			h ^= finish.getLong();
			h *= m;
		}
		h ^= h >>> r;
		h *= m;
		h ^= h >>> r;
		buf.order(byteOrder);
		return h;
	}
	
}
