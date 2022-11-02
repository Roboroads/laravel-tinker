Vagrant.configure("2") do |config|
  config.vm.box = "gusztavvargadr/windows-10"

  config.vm.provider "virtualbox" do |v|
    v.gui = true
    v.memory = 4096
    v.cpus = 2
    v.customize ["modifyvm", :id, "--vram", "256"]
    v.customize ["storageattach", :id,
                    "--storagectl", "IDE Controller",
                    "--port", "0", "--device", "1",
                    "--type", "dvddrive",
                    "--medium", "emptydrive"]
  end
end
