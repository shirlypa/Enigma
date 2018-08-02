package AgentDMParts;

import java.io.Serializable;

public class Data<T extends Serializable> implements Serializable {

    public enum  eDataType implements Serializable{
        SUCCESS_STRING,
        CLOSE,
        DICTIONERY,
        MACHINE,
        SOURCE,
        ALPHABET,
        MISSION_TODO
    }

    private T mData;
    private eDataType mDataType;
    public Data(T data,eDataType type)
    {
        this.mData=data;
        this.mDataType = type;
    }
    public T getmData() {
        return mData;
    }

    public void setmData(T mData) {
        this.mData = mData;
    }

    public eDataType getmDataType() {
        return mDataType;
    }

    public void setmDataType(eDataType mDataType) {
        this.mDataType = mDataType;
    }

}
