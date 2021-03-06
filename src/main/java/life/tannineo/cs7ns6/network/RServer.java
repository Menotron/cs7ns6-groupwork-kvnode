package life.tannineo.cs7ns6.network;

import com.alibaba.fastjson.JSON;
import com.alipay.remoting.BizContext;
import life.tannineo.cs7ns6.node.Node;
import life.tannineo.cs7ns6.node.entity.network.ClientKVReq;
import life.tannineo.cs7ns6.node.entity.network.Request;
import life.tannineo.cs7ns6.node.entity.network.Response;
import life.tannineo.cs7ns6.node.entity.param.EntryParam;
import life.tannineo.cs7ns6.node.entity.param.PeerChange;
import life.tannineo.cs7ns6.node.entity.param.RevoteParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * RPC Server
 */
public class RServer {

    private volatile Logger logger;

    private volatile boolean flag;

    private volatile int port;

    private Node node;

    private com.alipay.remoting.rpc.RpcServer rpcServer;

    public RServer(int port, Node node) {
        synchronized (this) {
            if (flag) {
                return;
            }

            this.port = port;

            logger = LoggerFactory.getLogger(RServer.class);

            rpcServer = new com.alipay.remoting.rpc.RpcServer(this.port, false, false);

            this.node = node;
            flag = true;

            rpcServer.registerUserProcessor(new RProcessor<Request>() {

                @Override
                public Object handleRequest(BizContext bizCtx, Request request) throws Exception {
                    return handlerRequest(request);
                }
            });
        }

    }

    public void start() {
        logger.info("server start at part : {}", this.port);
        rpcServer.startup();
    }

    public void stop() {
        rpcServer.shutdown();
    }

    public Response handlerRequest(Request request) {

        if (Boolean.TRUE.equals(node.getFailReq().get(request.getFromServer()))) {
            String str = "rejected by reject list: " + JSON.toJSONString(node.getFailReq());
            logger.info(str);
            throw new RuntimeException(str);
        }

        if (request.getCmd() == Request.R_VOTE) {
            return new Response(node.handlerRequestVote((RevoteParam) request.getObj()));
        } else if (request.getCmd() == Request.A_ENTRIES) {
            return new Response(node.handlerAppendEntries((EntryParam) request.getObj()));
        } else if (request.getCmd() == Request.CLIENT_REQ) {
            return new Response(node.handlerClientRequest((ClientKVReq) request.getObj()));
        } else if (request.getCmd() == Request.CHANGE_CONFIG_ADD) {
            return new Response(node.handlerConfigChangeAdd((PeerChange) request.getObj()));
        } else if (request.getCmd() == Request.CHANGE_CONFIG_REMOVE) {
            return new Response(node.handlerConfigChangeRemove((PeerChange) request.getObj()));
        } else if (request.getCmd() == Request.GET_CONFIG) {
            return new Response(node.handlerGetConfig());
        } else if (request.getCmd() == Request.SET_FAIL) {
            return new Response(node.handlerSetFail((PeerChange) request.getObj()));
        }
        return null;
    }


}
