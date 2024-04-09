Vagrant.configure("2") do |config|
  config.vm.box = "gusztavvargadr/windows-11"

  config.vm.provider "virtualbox" do |v|
    v.gui = true
    v.memory = 8192
    v.cpus = 4
    v.customize ["modifyvm", :id, "--vram", "256", "--monitorcount", "1"]
    v.customize ["storageattach", :id,
                 "--storagectl", "IDE Controller",
                 "--port", "0", "--device", "1",
                 "--type", "dvddrive",
                 "--medium", "emptydrive"]
  end

  config.vm.provision "shell", privileged: "true", powershell_elevated_interactive: "true", inline: <<-SHELL
       choco install -y git intellijidea-ultimate php
       choco install -y correttojdk --version=20.0.0
  SHELL
end
