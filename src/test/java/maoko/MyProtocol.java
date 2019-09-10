package maoko;

/**
 * @author maoko
 * @date 2019/9/10 18:09
 */

import maoko.common.exception.NotContainException;
import maoko.net.ifs.IByteBuff;
import maoko.net.protocol.IProtocol;

public class MyProtocol implements IProtocol {
    @Override
    public int getDeviceID() throws NotContainException {
        return 0;
    }

    @Override
    public int getTotalLen() {
        return 0;
    }

    @Override
    public byte[] getDatas() {
        return new byte[0];
    }

    @Override
    public byte[] getChilddatas() {
        return new byte[0];
    }

    @Override
    public void validate() throws Exception {

    }

    @Override
    public void readHeader(IByteBuff in) throws Exception {

    }

    @Override
    public boolean headerReadEnough() {
        return false;
    }

    @Override
    public boolean dataReadEnough() {
        return false;
    }

    @Override
    public void readData(IByteBuff in) {

    }

    @Override
    public boolean enderReadEnough() {
        return false;
    }

    @Override
    public void readEnd(IByteBuff in) {

    }

    @Override
    public void parseHeaders() throws Exception {

    }

    @Override
    public String srcHex() {
        return null;
    }

    @Override
    public IProtocol copyProtocol() throws Exception {
        return null;
    }

    @Override
    public byte[] buildBytes() throws Exception {
        return new byte[0];
    }

    @Override
    public void parse() throws Exception {

    }
}

