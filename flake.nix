{
  inputs = {
    nixpkgs.url = "github:NixOS/nixpkgs/nixpkgs-unstable";

    flake-utils.url = "github:numtide/flake-utils";

  };
  outputs = { self, nixpkgs, flake-utils, ...}:

  flake-utils.lib.eachDefaultSystem (system:
  let
    pkgs = import nixpkgs { inherit system; };
    # jar = pkgs.callPackage ./.jar.nix { };
    # jdtls = pkgs.callPackage ./.jdtls.nix { };
  in
  rec {
    devShell = with pkgs; mkShellNoCC {
      name = "java";
      buildInputs = [
        jdk
        nodejs
      ];
    };
  });
}
