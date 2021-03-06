package jp.ameba.mongo.protocol;

import org.bson.BSONEncoder;
import org.bson.BSONObject;
import org.bson.BasicBSONObject;

/**
 * OP_QUERY リクエストメッセージ
 * 
 * @author suguru
 */
public class Query extends Request {
	
	private int numberToSkip;
	
	private int numberToReturn;
	
	private BSONObject query;
	
	private BSONObject returnFieldSelector;
	
	private int flags;
	
	public Query(
			String databaseName,
			String collectionName,
			int numberToSkip,
			int numberToReturn,
			BSONObject query) {
		this(databaseName, collectionName, numberToSkip, numberToReturn, query, null);
	}
			
	
	public Query(
			String databaseName,
			String collectionName,
			int numberToSkip,
			int numberToReturn,
			BSONObject query,
			BSONObject returnFieldSelector) {
		super(OperationCode.OP_QUERY, databaseName, collectionName);
		this.numberToSkip = numberToSkip;
		this.numberToReturn = numberToReturn;
		this.query = query;
		this.returnFieldSelector = returnFieldSelector;
		this.consistency = Consistency.NONE;
	}
	
	public Query tailableCursor(boolean tailable) {
		this.flags = BitWise.addBit(flags, 1);
		return this;
	}
	
	public Query slaveOk(boolean slaveOk) {
		this.flags = BitWise.addBit(flags, 2);
		return this;
	}
	
	public Query oplogReplay(boolean oplogReplay) {
		this.flags = BitWise.addBit(flags, 3);
		return this;
	}
	
	public Query noCursorTimeout(boolean noCursorTimeout) {
		this.flags = BitWise.addBit(flags, 4);
		return this;
	}
	
	public Query awaitData(boolean awaitData) {
		this.flags = BitWise.addBit(flags, 5);
		return this;
	}
	
	public Query exhaust(boolean exhaust) {
		this.flags = BitWise.addBit(flags, 6);
		return this;
	}
	
	public Query numberToReturn(int numberToReturn) {
		this.numberToReturn = numberToReturn;
		return this;
	}
	
	public Query numberToSkip(int numberToSkip) {
		this.numberToSkip = numberToSkip;
		return this;
	}
	
	public Query where(String key, Object value) {
		this.query.put(key, value);
		return this;
	}
	
	public Query field(String key) {
		if (returnFieldSelector == null) {
			returnFieldSelector = new BasicBSONObject();
		}
		returnFieldSelector.put(key, 1);
		return this;
	}
	
	public int getNumberToReturn() {
		return numberToReturn;
	}
	
	public int getNumberToSkip() {
		return numberToSkip;
	}

	@Override
	public void encode(BSONEncoder encoder) {
		// Body
		encoder.writeInt(flags);
		encoder.writeCString(fullCollectionName);
		encoder.writeInt(numberToSkip);
		encoder.writeInt(numberToReturn);
		encoder.putObject(query);
		if (returnFieldSelector != null) {
			encoder.putObject(returnFieldSelector);
		}
	}
}
