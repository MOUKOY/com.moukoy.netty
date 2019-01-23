package soft.net.sconectclient;

import soft.net.ifs.IBytesBuild;
import soft.net.model.ClientChanel;

public class ClientSendDataImp implements IClientSendData {

	IBytesBuild data;

	public ClientSendDataImp(IBytesBuild data) {
		this.data = data;
	}

	@Override
	public void sendData(ClientChanel channel) throws Exception {
		try {
			if (channel != null)
				channel.sendData(data, true);
		} finally {
			if (channel != null)
				channel.close();
		}
	}

}
