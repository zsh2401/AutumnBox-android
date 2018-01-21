package top.atmb.autumnbox.acp;

import top.atmb.autumnbox.acp.processor.ACPDataBuilder;

public interface ACPResponser{
    ACPDataBuilder onRequestReceived(String command);
}