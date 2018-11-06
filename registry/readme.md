# Registry module for Wallet

Note:  this is work in progress and subject to change

This module is intended as a simple mechanism to track wallets. Normally we would have a p2p network between the wallets themselves. Having a central tracking point sidesteps the complexity of that.

With this idea we have the following features:
* register a new wallet, with name and port number
* unregister a wallet
* get a list of all wallets